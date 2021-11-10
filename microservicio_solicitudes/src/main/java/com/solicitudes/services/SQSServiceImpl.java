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
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@Service
public class SQSServiceImpl implements ISQSService {

	@Autowired
	ISolicitudService iSolicitudService;

	@Autowired
	ISolicitudDao iSolicitudDao;

	@Value("${cloud.aws.sqs.endpoint}")
	private String urlSqs;

	private final QueueMessagingTemplate queueMessagingTemplate;

	String token;

	public SQSServiceImpl(QueueMessagingTemplate queueMessagingTemplate) {
		this.queueMessagingTemplate = queueMessagingTemplate;
	}

	public void pushSqsSolicitud(String mensaje, String token) throws Exception {
		try {
			this.token = token;
			queueMessagingTemplate.send(urlSqs, MessageBuilder.withPayload(mensaje).build());

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	@SqsListener("Queue-solicitudes")
	void receiveSqsSolicitud(String mensaje) throws Exception {
		String idRevisorAsignar = "";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Se obtiene los usuarios con rol--> revisor para asignar las solicitudes
			List<UsuarioDto> usuariosRevisores = iSolicitudService.obtenerUsuariosRevisores(token);

			Map<String, Integer> mapAsignacionPorRevisor = iSolicitudDao.countSolicitudesAsignadasPorRevisor(usuariosRevisores);

			int cargaAsignacionTmp = 0;
			if (mapAsignacionPorRevisor.isEmpty()) {
				if (!usuariosRevisores.isEmpty()) {
					idRevisorAsignar= usuariosRevisores.get(0).getIdUsuario();
				}

			} else {
				for (String map : mapAsignacionPorRevisor.keySet()) {

					if (cargaAsignacionTmp == 0 || cargaAsignacionTmp >= mapAsignacionPorRevisor.get(map)) {
						cargaAsignacionTmp = mapAsignacionPorRevisor.get(map);
						idRevisorAsignar = map;
					}
				}
			}
			Solicitud solicitud = objectMapper.readValue(mensaje, Solicitud.class);

			solicitud.setEstado("ASIGNADA");
			solicitud.setIdUsuarioRevisor(idRevisorAsignar);
			
			long idSolicitud = iSolicitudDao.crearSolicitud(solicitud);

		} catch (Exception e) {
			pushSqsSolicitud(mensaje, token);
			throw GeneralException.throwException(this, e);
		}
	}

}
