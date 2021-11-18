package com.solicitudes.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.excepciones.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solicitudes.dao.ISolicitudDao;
import com.solicitudes.dto.DocumentoRequest;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Documento;
import com.solicitudes.model.Solicitud;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
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

	private final String urlSqsDocumentos = "https://sqs.us-east-2.amazonaws.com/505040459445/Queue-documentos";

	private final QueueMessagingTemplate queueMessagingTemplate;

	String token;

	public SQSServiceImpl(QueueMessagingTemplate queueMessagingTemplate) {
		this.queueMessagingTemplate = queueMessagingTemplate;
	}

	public void pushSqsSolicitud(String mensaje, String token) throws Exception {
		try {
			this.token = token;
			queueMessagingTemplate.send(urlSqsSolicitudes, MessageBuilder.withPayload(mensaje).build());

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	public void pushSqsDocumento(String mensaje, String token) throws Exception {
		try {
			this.token = token;
			queueMessagingTemplate.send(urlSqsDocumentos, MessageBuilder.withPayload(mensaje).build());

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener("Queue-solicitudes")
	void receiveSqsSolicitud(String mensaje) throws Exception {
		String idRevisorAsignar = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {

			Solicitud solicitud = objectMapper.readValue(mensaje, Solicitud.class);

			// Se obtiene los documentos asociados a la solicitud
			Documento documentoAdjunto = iDocumentoService.getDocumentoPorCriterios(solicitud.getTipoIdentificacion(),
					solicitud.getNumeroIdentificacion(), solicitud.getEmail());
			
			if (documentoAdjunto != null) {

				// Se obtiene los usuarios con rol--> revisor para asignar las solicitudes
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

				solicitud.setIdDocumentosAdjuntos(documentoAdjunto.getId());
				solicitud.setEstado("ASIGNADA");
				solicitud.setIdUsuarioRevisor(idRevisorAsignar);

				long idSolicitud = iSolicitudDao.crearSolicitud(solicitud);
			} else {
				// Se debe enviar correo con notificacion solicitud fallida
			}

		} catch (Exception e) {
			pushSqsSolicitud(mensaje, token);
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener("Queue-documentos")
	void receiveSqsDocumento(String mensaje) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			DocumentoRequest documento = objectMapper.readValue(mensaje, DocumentoRequest.class);

			// Se valida la cedula con el servicio de aws recognition
			boolean isDocumentValid = iDocumentoService.validarDocumento(documento.getCedula(), token);

			if (isDocumentValid) {
				String idDocumentoMongo = iDocumentoService.crearDocumento(documento.getCedula(),
						documento.getHistoriaClinica(), documento.getEmail(), documento.getTipoIdentificacion(),
						documento.getNumeroIdentificacion());
			}

		} catch (Exception e) {
			pushSqsDocumento(mensaje, token);
			throw GeneralException.throwException(this, e);
		}
	}
}
