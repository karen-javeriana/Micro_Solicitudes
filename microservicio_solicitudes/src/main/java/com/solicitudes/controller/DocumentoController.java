package com.solicitudes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solicitudes.dto.DocumentoRequest;
import com.solicitudes.dto.DocumentoResponse;
import com.solicitudes.dto.ErrorDto;
import com.solicitudes.model.Documento;
import com.solicitudes.services.IDocumentoService;
import com.solicitudes.services.ISQSService;
import com.solicitudes.services.ITokenService;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Api de documentos")
public class DocumentoController {

	@Autowired
	IDocumentoService documentoService;

	@Autowired
	ITokenService tokenService;

	@Autowired
	ISQSService iSqsService;

	@Timed("get.documentos")
	@ApiOperation(value = "Devuelve un objeto documento dado su id", response = Documento.class)
	@GetMapping(value = "/documento/{idDocumentoAdjunto}")
	public ResponseEntity<DocumentoResponse> obtenerDocumentosAdjuntos(
			@ApiParam(value = "Identificador del documento adjunto a consultar", required = true) @PathVariable("idDocumentoAdjunto") String idDocumentoAdjunto,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
			throws Exception {
		ResponseEntity<DocumentoResponse> response = null;

		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {
					Documento documento = documentoService.getDocumentoPorId(idDocumentoAdjunto);
					if (documento != null && documento.getId() != null) {
						response = new ResponseEntity<>(new DocumentoResponse(documento, null, null, null, true),
								HttpStatus.OK);
					} else {
						response = new ResponseEntity<>(
								new DocumentoResponse("No hay documentos asociados al id", false), HttpStatus.OK);
					}
				}
			} else {
				response = new ResponseEntity<>(new DocumentoResponse("Ocurrio un error validando la sesion", false),
						HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			ErrorDto error = documentoService.setMessageExceptionRequest(e);
			HttpStatus status = null;

			if (error.getCodeError().equals("VD01")) {
				status = HttpStatus.OK;
			} else if (error.getCodeError().equals("AUT01")) {
				status = HttpStatus.UNAUTHORIZED;

			} else if (error.getCodeError().equals("BD01")) {
				status = HttpStatus.REQUEST_TIMEOUT;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
			response = new ResponseEntity<>(new DocumentoResponse(error.getDescripcionError(), false), status);
		}
		return response;
	}

	@Timed("post.documentos")
	@ApiOperation(value = "Crea y procesa los documentos adjuntos", response = Boolean.class)
	@PostMapping(value = "/documento")
	public ResponseEntity<DocumentoResponse> crearDocumento(
			@ApiParam(value = "Objeto json para procesar los documentos", required = true) @RequestBody DocumentoRequest request,
			@ApiParam(value = "Campo para validar la sesion (token)", required = true) @RequestHeader("Authorization") String auth)
			throws Exception {

		ResponseEntity<DocumentoResponse> response = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (auth != null && auth.startsWith("Bearer")) {
				String[] partsToken = auth.split(" ");
				boolean isTokenValid = tokenService.isTokenValid(partsToken[1]);
				if (isTokenValid) {

					String documentoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
					// Se inserta en la cola de documentos
					iSqsService.pushSqsDocumentoFifo(documentoJson, auth);

					response = new ResponseEntity<>(new DocumentoResponse(null, null, null, null, true), HttpStatus.OK);
				}
			} else {
				response = new ResponseEntity<>(new DocumentoResponse("Ocurrio un error validando la sesion", false),
						HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			ErrorDto error = documentoService.setMessageExceptionRequest(e);
			HttpStatus status = null;

			if (error.getCodeError().equals("VD01")) {
				status = HttpStatus.OK;
			} else if (error.getCodeError().equals("AUT01")) {
				status = HttpStatus.UNAUTHORIZED;

			} else if (error.getCodeError().equals("BD01")) {
				status = HttpStatus.REQUEST_TIMEOUT;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
			response = new ResponseEntity<>(new DocumentoResponse(error.getDescripcionError(), false), status);
		}
		return response;
	}
}
