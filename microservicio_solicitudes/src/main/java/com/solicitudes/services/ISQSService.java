package com.solicitudes.services;

public interface ISQSService {

	void pushSqsSolicitud(String mensaje) throws Exception;
	
	void pushSqsDocumentoFifo(String mensaje) throws Exception;
}
