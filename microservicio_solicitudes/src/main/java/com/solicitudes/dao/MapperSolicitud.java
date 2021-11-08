package com.solicitudes.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.solicitudes.model.Solicitud;

public class MapperSolicitud implements RowMapper<Solicitud> {

	@Override
	public Solicitud mapRow(ResultSet rs, int rowNum) throws SQLException {
		Solicitud solicitud = new Solicitud();
		try {
			solicitud.setIdSolicitud(rs.getInt("idSolicitud"));
			solicitud.setIdProducto(rs.getString("idProducto"));
			solicitud.setEstado(rs.getString("estado"));
			solicitud.setDescripcion(rs.getString("descripcion"));
			solicitud.setIdDocumentosAdjuntos(rs.getString("idDocumentosAdjuntos"));
			solicitud.setIdUsuarioRevisor(rs.getString("idUsuarioRevisor"));
			solicitud.setFechaRevision(rs.getTimestamp("fechaRevision"));
			solicitud.setFechaSolicitud(rs.getTimestamp("fechaSolicitud"));
			solicitud.setNombresCliente(rs.getString("nombresCliente"));
			solicitud.setApellidosCliente(rs.getString("apellidosCliente"));
			solicitud.setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
			solicitud.setTipoIdentificacion(rs.getString("tipoIdentificacion"));
			solicitud.setEmail(rs.getString("email"));
			solicitud.setFoto(rs.getString("foto"));
			solicitud.setTelefono(rs.getString("telefono"));
			solicitud.setFechaNacimiento("fechaNacimiento");
			solicitud.setCiudad(rs.getString("ciudad"));
			solicitud.setPais(rs.getString("pais"));
			solicitud.setDireccion(rs.getString("direccion"));
			solicitud.setGenero(rs.getString("genero"));
			solicitud.setIdCliente(rs.getString("idCliente"));

		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		return solicitud;
	}

}
