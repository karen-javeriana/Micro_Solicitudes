package com.solicitudes.services;

import com.solicitudes.model.Documento;

public interface IMongoService {

	String crearDocumento(String cedula, String historiaClinica, String nombreCliente) throws Exception;
	
	void deleteDocumento(String id) throws Exception;
	
	Documento getDocumentoPorId(String id) throws Exception;

}
