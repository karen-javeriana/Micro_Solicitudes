package com.solicitudes.services;

import com.solicitudes.dto.ErrorDto;
import com.solicitudes.model.Documento;

public interface IDocumentoService {

	String crearDocumento(String cedula, String historiaClinica, String email, String idGenerado) throws Exception;

	void deleteDocumento(String id) throws Exception;

	Documento getDocumentoPorId(String id) throws Exception;

	ErrorDto setMessageExceptionRequest(Exception ex);

	boolean validarDocumento(String cedula, String token) throws Exception;

	Documento getDocumentoPorIdGenerado(String idGenerado) throws Exception;

}
