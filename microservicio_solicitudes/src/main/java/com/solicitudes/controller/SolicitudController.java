package com.solicitudes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.excepciones.ValidacionDatosException;
import com.solicitudes.dto.ErrorDto;
import com.solicitudes.dto.SolicitudRequest;
import com.solicitudes.dto.SolicitudResponse;
import com.solicitudes.model.Solicitud;
import com.solicitudes.services.IMongoService;
import com.solicitudes.services.ISolicitudService;
import com.solicitudes.services.ITokenService;

import io.micrometer.core.annotation.Timed;

@RestController
@CrossOrigin(origins = "*")
public class SolicitudController {

	@Autowired
	ISolicitudService solicitudService;

	@Autowired
	IMongoService mongoService;

	@Autowired
	RestTemplate template;

	@Autowired
	ITokenService tokenService;

	@Timed("get.solicitudes")
	@GetMapping(value = "/solicitud/{idUsuarioRevisor}")
	public SolicitudResponse obtenerSolicitudPorIdUsuarioRevisor(
			@PathVariable("idUsuarioRevisor") String idUsuarioRevisor, @RequestHeader("Authorization") String auth)
			throws Exception {
		SolicitudResponse response = new SolicitudResponse();
		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {
					List<SolicitudRequest> listSolicitud = solicitudService
							.obtenerSolicitudPorIdUsuarioRevisor(idUsuarioRevisor);
					response.setListSolicitudes(listSolicitud);
				}
			} else {
				throw new ValidacionDatosException("Ocurrio un error validando la sesion");
			}

		} catch (

		Exception e) {
			ErrorDto error = solicitudService.setMessageExceptionRequest(e);
			response.setError(error.getDescripcionError());
			response.setSucess(false);

		}
		return response;
	}

	@Timed("post.solicitudes")
	@PostMapping(value = "/solicitud")
	public SolicitudResponse crearSolicitud(@RequestBody SolicitudRequest request,
			@RequestHeader("Authorization") String auth) throws Exception {

		SolicitudResponse response = new SolicitudResponse();
		Solicitud entidadSolicitud = new Solicitud();
		String idDocumento = null;
		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {
					entidadSolicitud.setIdProducto(request.getIdProducto());
					entidadSolicitud.setDescripcion(request.getDescripcion());
					entidadSolicitud.setNombresCliente(request.getNombresCliente());
					entidadSolicitud.setApellidosCliente(request.getApellidosCliente());
					entidadSolicitud.setNumeroIdentificacion(request.getNumeroIdentificacion());
					entidadSolicitud.setTipoIdentificacion(request.getTipoIdentificacion());
					entidadSolicitud.setEmail(request.getEmail());
					entidadSolicitud.setFoto(request.getFoto());
					entidadSolicitud.setTelefono(request.getTelefono());
					entidadSolicitud.setFechaNacimiento(request.getFechaNacimiento());
					entidadSolicitud.setCiudad(request.getCiudad());
					entidadSolicitud.setPais(request.getPais());
					entidadSolicitud.setIdCliente(request.getIdCliente());
					entidadSolicitud.setDireccion(request.getDireccion());
					entidadSolicitud.setGenero(request.getGenero());

					idDocumento = mongoService.crearDocumento(request.getCedulaAdjunta(),
							request.getHistoriaClinicaAdjunta(),
							entidadSolicitud.getNombresCliente() + " " + entidadSolicitud.getApellidosCliente());

					entidadSolicitud.setIdDocumentosAdjuntos(idDocumento);
					solicitudService.crearSolicitud(entidadSolicitud, partsToken[1]);
					response.setSucess(true);
				}
			} else {
				throw new ValidacionDatosException("Ocurrio un error validando la sesion");
			}

		} catch (Exception e) {
			ErrorDto error = solicitudService.setMessageExceptionRequest(e);
			response.setError(error.getDescripcionError());
			response.setSucess(false);
			if (idDocumento != null) {
				mongoService.deleteDocumento(idDocumento);
			}
		}
		return response;
	}

	@Timed("put.solicitudes")
	@PutMapping(value = "/solicitud/{id}")
	public SolicitudResponse actualizarSolicitud(@RequestBody Solicitud solicitud, @PathVariable("id") int id,
			@RequestHeader("Authorization") String auth) throws Exception {
		SolicitudResponse response = new SolicitudResponse();
		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {
					solicitudService.actualizarSolicitud(solicitud, id);
					response.setSucess(true);
				}
			} else {
				throw new ValidacionDatosException("Ocurrio un error validando la sesion");
			}

		} catch (Exception e) {
			ErrorDto error = solicitudService.setMessageExceptionRequest(e);
			response.setError(error.getDescripcionError());
			response.setSucess(false);
		}
		return response;
	}

	@Timed("get.documentos")
	@GetMapping(value = "/solicitud/adjuntos/{idDocumentoAdjunto}")
	public SolicitudResponse obtenerDocumentosAdjuntos(@PathVariable("idDocumentoAdjunto") String idDocumentoAdjunto,
			@RequestHeader("Authorization") String auth) throws Exception {
		SolicitudResponse response = new SolicitudResponse();

		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {
					response.setDocumento(mongoService.getDocumentoPorId(idDocumentoAdjunto));
					response.setSucess(true);
				}
			} else {
				throw new ValidacionDatosException("Ocurrio un error validando la sesion");
			}

		} catch (Exception e) {
			ErrorDto error = solicitudService.setMessageExceptionRequest(e);
			response.setError(error.getDescripcionError());
			response.setSucess(false);
		}
		return response;
	}
}
