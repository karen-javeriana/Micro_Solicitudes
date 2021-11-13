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
import com.solicitudes.model.Documento;
import com.solicitudes.model.Solicitud;
import com.solicitudes.services.IMongoService;
import com.solicitudes.services.ISolicitudService;
import com.solicitudes.services.ITokenService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Microservicio de Solicitudes")
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
	@ApiOperation(value = "Retorna las solicitudes asignadas a un usuario revisor", response = List.class)
	@GetMapping(value = "/solicitud/{idUsuarioRevisor}")
	public SolicitudResponse obtenerSolicitudPorIdUsuarioRevisor(
			@ApiParam(value = "Identificador del usuario revisor a consultar", required = true) @PathVariable("idUsuarioRevisor") String idUsuarioRevisor,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
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
				throw new ValidacionDatosException("Ocurrio un error validando el token de la sesion");
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
	@ApiOperation(value = "Crea las solicitudes", response = Boolean.class)
	@PostMapping(value = "/solicitud")
	public SolicitudResponse crearSolicitud(
			@ApiParam(value = "Objeto json para crear la solicitud", required = true) @RequestBody SolicitudRequest request,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
			throws Exception {

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
	@ApiOperation(value = "Actualiza una solicitud dado su id", response = Boolean.class)
	@PutMapping(value = "/solicitud/{id}")
	public SolicitudResponse actualizarSolicitud(
			@ApiParam(value = "Objeto json para actualizar la solicitud", required = true) @RequestBody Solicitud solicitud,
			@ApiParam(value = "Identificador de la solicitud a actualizar", required = true) @PathVariable("id") int id,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
			throws Exception {
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
	@ApiOperation(value = "Devuelve una objeto documento dado su id", response = Documento.class)
	@GetMapping(value = "/solicitud/adjuntos/{idDocumentoAdjunto}")
	public SolicitudResponse obtenerDocumentosAdjuntos(
			@ApiParam(value = "Identificador del documento adjunto a consultar", required = true) @PathVariable("idDocumentoAdjunto") String idDocumentoAdjunto,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
			throws Exception {
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
