package com.solicitudes.services;

public interface ISQSService {

	void pushSqsSolicitud(String mensaje, String token) throws Exception;
}
