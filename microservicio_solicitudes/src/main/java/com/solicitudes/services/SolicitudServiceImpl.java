package com.solicitudes.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.excepciones.GeneralException;
import com.excepciones.ValidacionDatosException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solicitudes.dao.ISolicitudDao;
import com.solicitudes.dto.ErrorDto;
import com.solicitudes.dto.SolicitudRequest;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

@Service
public class SolicitudServiceImpl implements ISolicitudService {

	@Autowired
	ISolicitudDao iSolicitudDao;

	@Autowired
	RestTemplate template;

	@Autowired
	ISQSService iSqsService;

	public List<SolicitudRequest> obtenerSolicitudPorIdUsuarioRevisor(String idUsuarioRevisor) throws Exception {
		List<SolicitudRequest> listSolicitudes = new ArrayList<SolicitudRequest>();

		try {
			List<Solicitud> result = iSolicitudDao.obtenerSolicitudPorIdUsuarioRevisor(idUsuarioRevisor);
			if (listSolicitudes != null) {
				for (Solicitud sol : result) {
					SolicitudRequest solicitudRequest = new SolicitudRequest();
					solicitudRequest = this.convertToEntity(sol);
					listSolicitudes.add(solicitudRequest);
				}

			}

		} catch (Exception e) {

			throw GeneralException.throwException(this, e);
		}
		return listSolicitudes;
	}

	public void crearSolicitud(Solicitud solicitud, String token) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		try {
			if (token == null) {
				throw new ValidacionDatosException("El token es invalido");
			} else {
				String solicitudJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(solicitud);
				// Se inserta en la cola
				iSqsService.pushSqsSolicitud(solicitudJson, token);
			}

		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}

	}

	public void actualizarSolicitud(Solicitud solicitud, int id) throws Exception {

		try {
			iSolicitudDao.actualizarSolicitud(solicitud, id, "RESUELTA");
		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
	}

	public List<UsuarioDto> obtenerUsuariosRevisores(String token) throws Exception {
		List<UsuarioDto> listUsuarios = new ArrayList<UsuarioDto>();
		ObjectMapper mapper = new ObjectMapper();
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		try {
			headers.add("Authorization", "Bearer " + token);
			String url = "https://sb-identity.mybluemix.net/api/v1/sb/admins/user/?role=reviewer";

			String result = template.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();

			JsonNode usuariosNode = mapper.readTree(result);
			ArrayNode arrayNode = (ArrayNode) usuariosNode.get("users");

			if (arrayNode != null) {
				for (Object obj : arrayNode) {

					ObjectNode json = (ObjectNode) obj;

					listUsuarios.add(new UsuarioDto(json.get("_id").asText(), json.get("name").asText(),
							json.get("surname").asText(), json.get("email").asText(), json.get("address").asText(),
							json.get("phone").asText(), json.get("documentNumber").asText(),
							json.get("documentType").asText(), json.get("role").asText(), json.get("photo").asText(),
							json.get("city").asText(), json.get("gender").asText(), json.get("birthday").asText(),
							json.get("country").asText()));
				}
			} else {
				throw new ValidacionDatosException("No existen usuarios revisores");
			}
		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
		return listUsuarios;
	}

	public ErrorDto setMessageExceptionRequest(Exception ex) {

		ErrorDto infoError;

		if (ex instanceof GeneralException) {
			infoError = ((GeneralException) ex).getCamposError();
		} else {
			infoError = new ErrorDto();
			infoError.setDescripcionError(ex.getMessage());

		}
		return infoError;

	}

	private SolicitudRequest convertToEntity(Solicitud solicitudEntity) throws Exception {
		SolicitudRequest request = new SolicitudRequest();
		try {
			request.setNombresCliente(solicitudEntity.getNombresCliente());
			request.setIdSolicitud(solicitudEntity.getIdSolicitud());
			request.setIdProducto(solicitudEntity.getIdProducto());
			request.setEstado(solicitudEntity.getEstado());
			request.setDescripcion(solicitudEntity.getDescripcion());
			request.setFechaSolicitud(solicitudEntity.getFechaSolicitud());
			request.setFechaRevision(solicitudEntity.getFechaRevision());
			request.setIdDocumentosAdjuntos(solicitudEntity.getIdDocumentosAdjuntos());
			request.setIdUsuarioRevisor(solicitudEntity.getIdUsuarioRevisor());
			request.setNombresCliente(solicitudEntity.getNombresCliente());
			request.setApellidosCliente(solicitudEntity.getApellidosCliente());
			request.setNumeroIdentificacion(solicitudEntity.getNumeroIdentificacion());
			request.setEmail(solicitudEntity.getEmail());
			request.setFoto(solicitudEntity.getFoto());
			request.setTelefono(solicitudEntity.getTelefono());
			request.setFechaNacimiento(solicitudEntity.getFechaNacimiento());
			request.setCiudad(solicitudEntity.getCiudad());
			request.setPais(solicitudEntity.getPais());
			request.setIdCliente(solicitudEntity.getIdCliente());
			request.setDireccion(solicitudEntity.getDireccion());
			request.setGenero(solicitudEntity.getGenero());
		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
		return request;
	}

	public String autenticar() throws Exception {
		String token = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			String dir = "https://sb-identity.mybluemix.net/api/v1/sb/login/admin/";

			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			HttpEntity<String> request = new HttpEntity<String>(headers);
			headers.setBasicAuth("karen-calderon@javeriana.edu.co", "1234567890");

			String resultAuth = template.exchange(dir, HttpMethod.GET, request, String.class).getBody();
			JsonNode tokenNode = mapper.readTree(resultAuth);
			token = tokenNode.get("token").asText();
		} catch (Exception e) {
			throw GeneralException.throwException(this, e);
		}
		return token;
	}
}
