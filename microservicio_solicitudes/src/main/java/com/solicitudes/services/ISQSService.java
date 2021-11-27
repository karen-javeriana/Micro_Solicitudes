package com.solicitudes.services;

public interface ISQSService {

	void pushSqsSolicitud(String mensaje) throws Exception;
	
	void pushSqsDocumentos(String mensaje) throws Exception;
}
