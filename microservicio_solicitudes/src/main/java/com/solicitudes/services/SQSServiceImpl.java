package com.solicitudes.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.excepciones.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solicitudes.dao.ISolicitudDao;
import com.solicitudes.dto.DocumentoRequest;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Service
public class SQSServiceImpl implements ISQSService {

	@Autowired
	ISolicitudService iSolicitudService;

	@Autowired
	IDocumentoService iDocumentoService;

	@Autowired
	ISolicitudDao iSolicitudDao;

	@Value("${cloud.aws.sqs.endpoint}")
	private String urlSqsSolicitudes;

	private final String urlSqsDocumentosFifo = "https://sqs.us-east-2.amazonaws.com/505040459445/Queue_documento.fifo";

	private final QueueMessagingTemplate queueMessagingTemplate;

	String token;

	public SQSServiceImpl(QueueMessagingTemplate queueMessagingTemplate) {
		this.queueMessagingTemplate = queueMessagingTemplate;
	}

	public void pushSqsSolicitud(String mensaje, String token) throws Exception {
		try {
			this.token = token;
			Map<String, Object> headers = new HashMap<>();
			headers.put("message-group-id", "groupId1");
			headers.put("message-deduplication-id", "dedupId1");
			queueMessagingTemplate.convertAndSend(urlSqsSolicitudes, mensaje, headers);

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	public void pushSqsDocumentoFifo(String mensaje, String token) throws Exception {
		try {
			this.token = token;
			Map<String, Object> headers = new HashMap<>();
			headers.put("message-group-id", "groupId2");
			headers.put("message-deduplication-id", "dedupId2");
			queueMessagingTemplate.convertAndSend(urlSqsDocumentosFifo, mensaje, headers);

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener(value = "Queue_solicitud.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	void receiveSqsSolicitud(String mensaje) throws Exception {
		String idRevisorAsignar = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			Solicitud solicitud = objectMapper.readValue(mensaje, Solicitud.class);

			// Se obtiene los usuarios con rol--> revisor para asignar las solicitudes
			if (token == null) {
				token = iSolicitudService.autenticar();
			}
			List<UsuarioDto> usuariosRevisores = iSolicitudService.obtenerUsuariosRevisores(token);

			Map<String, Integer> mapAsignacionPorRevisor = iSolicitudDao
					.countSolicitudesAsignadasPorRevisor(usuariosRevisores);

			int cargaAsignacionTmp = 0;
			if (mapAsignacionPorRevisor.isEmpty()) {
				if (!usuariosRevisores.isEmpty()) {
					idRevisorAsignar = usuariosRevisores.get(0).getIdUsuario();
				}

			} else {
				for (String map : mapAsignacionPorRevisor.keySet()) {

					if (cargaAsignacionTmp == 0 || cargaAsignacionTmp >= mapAsignacionPorRevisor.get(map)) {
						cargaAsignacionTmp = mapAsignacionPorRevisor.get(map);
						idRevisorAsignar = map;
					}
				}
			}

			solicitud.setEstado("ASIGNADA");
			solicitud.setIdUsuarioRevisor(idRevisorAsignar);

			iSolicitudDao.crearSolicitud(solicitud);

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener(value = "Queue_documento.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	void receiveSqsDocumento(String mensaje) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			DocumentoRequest documento = objectMapper.readValue(mensaje, DocumentoRequest.class);

			Solicitud solicitud = iSolicitudService.obtenerSolicitudPorId(documento.getId());

			// Se valida la cedula con el servicio de aws recognition
			String credenciales = iSolicitudService.autenticar();
			boolean isDocumentValid = iDocumentoService.validarDocumento(documento.getCedula(), credenciales);

			if (isDocumentValid) {
				String idDocumentoMongo = iDocumentoService.crearDocumento(documento.getCedula(),
						documento.getHistoriaClinica(), documento.getEmail(), documento.getId());

				// Se actualiza el id Documento en la bd solicitud
				if (solicitud != null) {
					solicitud.setIdDocumentosAdjuntos(documento.getId());
					iSolicitudService.actualizarSolicitud(solicitud, documento.getId(), solicitud.getEstado());
				} else {
					// Se envia correo
				}
			} else {
				// Se notifica al usuario y se cambia el estado a la solicitud creada
				if (solicitud != null) {
					iSolicitudService.actualizarSolicitud(solicitud, documento.getId(), "RECHAZADA");
				}
			}

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}
}
