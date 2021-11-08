package com.solicitudes.dao;

import java.util.List;
import java.util.Map;

import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

public interface ISolicitudDao {

	List<Solicitud> obtenerSolicitudPorIdUsuarioRevisor(String idUsuarioRevisor) throws Exception;

	Long crearSolicitud(Solicitud solicitud) throws Exception;

	void actualizarSolicitud(Solicitud solicitud, int idSolicitud, String estado) throws Exception;

	Map<String, Integer> countSolicitudesAsignadasPorRevisor(List<UsuarioDto> listUsuarios) throws Exception;

	void actualizarSolicitudAsignada(int idSolicitud, String estado, String idRevisor) throws Exception;
}
