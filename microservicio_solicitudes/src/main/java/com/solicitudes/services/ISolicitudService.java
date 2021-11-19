package com.solicitudes.services;

import java.util.List;

import com.solicitudes.dto.ErrorDto;
import com.solicitudes.dto.SolicitudRequest;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

public interface ISolicitudService {

	List<SolicitudRequest> obtenerSolicitudPorIdUsuarioRevisor(String idUsuarioRevisor) throws Exception;

	void crearSolicitud(Solicitud solicitud, String token) throws Exception;

	void actualizarSolicitud(Solicitud solicitud, String id, String estado) throws Exception;

	List<UsuarioDto> obtenerUsuariosRevisores(String token) throws Exception;
	
	Solicitud obtenerSolicitudPorId(String id) throws Exception;

	ErrorDto setMessageExceptionRequest(Exception ex);
	
	String autenticar() throws Exception;

}
