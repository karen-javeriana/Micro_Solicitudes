package com.solicitudes.services;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.excepciones.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.solicitudes.dao.ISolicitudDao;
import com.solicitudes.dto.CorreoGenericoDto;
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

	private final String urlSqsDocumentos = "https://sqs.us-east-2.amazonaws.com/505040459445/Queue-documentos";

	private final QueueMessagingTemplate queueMessagingTemplate;

	public SQSServiceImpl(QueueMessagingTemplate queueMessagingTemplate) {
		this.queueMessagingTemplate = queueMessagingTemplate;
	}

	public void pushSqsSolicitud(String mensaje) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Solicitud solicitud = mapper.readValue(mensaje, Solicitud.class);

			Map<String, Object> headers = new HashMap<>();
			headers.put("message-group-id", "groupId" + solicitud.getId());
			headers.put("message-deduplication-id", "dedupId" + solicitud.getId());
			queueMessagingTemplate.convertAndSend(urlSqsSolicitudes, mensaje, headers);

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	public void pushSqsDocumentos(String mensaje) throws Exception {
		try {
			queueMessagingTemplate.send(urlSqsDocumentos, MessageBuilder.withPayload(mensaje).build());

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener(value = "Queue_solicitudes.fifo", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
	void receiveSqsSolicitud(String mensaje) throws Exception {
		String idRevisorAsignar = "";
		ObjectMapper objectMapper = new ObjectMapper();
		boolean validacionSarflaft = false;
		Double resultadoSarlaft = 0.0;
		try {

			Solicitud solicitud = objectMapper.readValue(mensaje, Solicitud.class);

			// Se obtiene los usuarios con rol--> revisor para asignar las solicitudes

			String token = iSolicitudService.autenticar();
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

			try {
				resultadoSarlaft = iSolicitudService.obtenerScoreSarlaft();
				validacionSarflaft = true;
			} catch (Exception e) {
				validacionSarflaft = false;
			}

			if (resultadoSarlaft < 90.0 || !validacionSarflaft) {
				solicitud.setEstado("RECHAZADA");
			} else {
				solicitud.setEstado("PENDIENTE");
				solicitud.setScoreSarlaft(resultadoSarlaft);
				solicitud.setIdUsuarioRevisor(idRevisorAsignar);
			}
			iSolicitudDao.crearSolicitud(solicitud);

		} catch (Exception e) {
			if ((!(e.getCause() instanceof SQLIntegrityConstraintViolationException))
					|| !(e.getCause() instanceof MysqlDataTruncation)) {
				pushSqsSolicitud(mensaje);
			}
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener(value = "Queue-documentos", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
	void receiveSqsDocumento(String mensaje) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			DocumentoRequest documento = objectMapper.readValue(mensaje, DocumentoRequest.class);

			Solicitud solicitud = iSolicitudService.obtenerSolicitudPorId(documento.getId());

			// Se valida la cedula con el servicio de aws recognition
			if (solicitud != null && !solicitud.getEstado().equals("RECHAZADA")) {
				String token = iSolicitudService.autenticar();

				boolean isDocumentValid = iDocumentoService.validarDocumento(documento.getCedula(), token);

				if (isDocumentValid) {
					String idDocumentoMongo = iDocumentoService.crearDocumento(documento.getCedula(),
							documento.getHistoriaClinica(), documento.getEmail(), documento.getId(), "VALIDO");

					// Se actualiza el id Documento en la bd solicitud
					solicitud.setIdDocumentosAdjuntos(documento.getId());
					solicitud.setEstado("ASIGNADA");
					iSolicitudService.actualizarSolicitud(solicitud, documento.getId(), solicitud.getEstado());
					
					CorreoGenericoDto infoCorreo = new CorreoGenericoDto("Estimado Cliente",
							"Su solicitud fue asignada correctamente, pronto uno de nuestros asesores se pondra en contacto",
							"Notificación asignación solicitud", "generic", documento.getEmail(), "");
					iSolicitudService.enviarCorreoNotificacion(infoCorreo);
					
				} else {
					iDocumentoService.crearDocumento(documento.getCedula(), documento.getHistoriaClinica(),
							documento.getEmail(), documento.getId(), "INVALIDO");

					// Se notifica al usuario y se cambia el estado a la solicitud RECHAZADA
					if (solicitud != null) {
						iSolicitudService.actualizarSolicitud(solicitud, documento.getId(), "RECHAZADA");
					}

					// Envio correo de rechazo
					CorreoGenericoDto infoCorreo = new CorreoGenericoDto("Estimado Cliente",
							"Su solicitud ha sido rechazada, por favor verifique la información suministrada",
							"Notificación solicitud rechazada", "generic", documento.getEmail(), "");
					iSolicitudService.enviarCorreoNotificacion(infoCorreo);
				}
			} else {
				// Se envia correo, de rechazo
				CorreoGenericoDto infoCorreo = new CorreoGenericoDto("Estimado Cliente",
						"Su solicitud ha sido rechazada, por favor verifique la información suministrada",
						"Notificación solicitud rechazada", "generic", documento.getEmail(), "");
				iSolicitudService.enviarCorreoNotificacion(infoCorreo);

			}

		} catch (Exception e) {
			pushSqsDocumentos(mensaje);
			throw GeneralException.throwException(this, e);
		}
	}
}
