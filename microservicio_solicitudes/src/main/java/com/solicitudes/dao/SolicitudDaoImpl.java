package com.solicitudes.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import com.excepciones.BaseDatosException;
import com.excepciones.GeneralException;
import com.excepciones.ValidacionDatosException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

@Repository
public class SolicitudDaoImpl implements ISolicitudDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Solicitud> obtenerSolicitudPorIdUsuarioRevisor(String idUsuarioRevisor) throws Exception {
		try {
			List<Solicitud> solicitudes = jdbcTemplate.query(
					"select * from Solicitud WHERE idUsuarioRevisor = ? and estado ='ASIGNADA' ", new MapperSolicitud(),
					idUsuarioRevisor);

			if (solicitudes.size() > 0) {
				return solicitudes;
			} else {
				throw new ValidacionDatosException("No existe un Revisor asociado al id");
			}
		} catch (Exception ex) {

			if (ex.getCause() instanceof CommunicationsException) {

				throw GeneralException.throwException(this, ex,
						"Error estableciendo comunicación con la base de datos");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	public Long crearSolicitud(Solicitud solicitud) throws Exception {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
				.withTableName("Solicitud").usingGeneratedKeyColumns("idSolicitud");
		long id = 0;
		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("idProducto", solicitud.getIdProducto());
			parameters.put("estado", solicitud.getEstado() == null ? "SIN ASIGNAR" : solicitud.getEstado());
			parameters.put("descripcion", solicitud.getDescripcion());
			parameters.put("idDocumentosAdjuntos", solicitud.getIdDocumentosAdjuntos());
			parameters.put("fechaSolicitud", new Date());
			parameters.put("idUsuarioRevisor",solicitud.getIdUsuarioRevisor());
			parameters.put("fechaRevision", null);
			parameters.put("nombresCliente", solicitud.getNombresCliente());
			parameters.put("apellidosCliente", solicitud.getApellidosCliente());
			parameters.put("numeroIdentificacion", solicitud.getNumeroIdentificacion());
			parameters.put("tipoIdentificacion", solicitud.getTipoIdentificacion());
			parameters.put("email", solicitud.getEmail());
			parameters.put("foto", solicitud.getFoto());
			parameters.put("telefono", solicitud.getTelefono());
			parameters.put("fechaNacimiento", solicitud.getFechaNacimiento());
			parameters.put("ciudad", solicitud.getCiudad());
			parameters.put("pais", solicitud.getPais());
			parameters.put("idCliente", solicitud.getIdCliente());
			parameters.put("direccion", solicitud.getDireccion());
			parameters.put("genero", solicitud.getGenero());

			id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

			if (id == 0) {
				throw new BaseDatosException("Error creando la solicitud");
			}
		} catch (Exception ex) {
			if (ex instanceof DataAccessResourceFailureException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo comunicación con la base de datos");
			}
			throw GeneralException.throwException(this, ex);
		}
		return id;

	}

	public void actualizarSolicitud(Solicitud solicitud, int id, String estado) throws Exception {
		try {
			String updateQuery = "update Solicitud set fechaRevision = ? , estado = ? where idSolicitud = ?";
			jdbcTemplate.update(updateQuery, new Date(), estado, id);
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo comunicación con la base de datos");
			}
			throw new BaseDatosException("Error actualizando la solicitud");
		}
	}

	public Map<String, Integer> countSolicitudesAsignadasPorRevisor(List<UsuarioDto> listUsuarios) throws Exception {

		Map<String, Integer> mapResult = new HashMap<String, Integer>();
		try {
			String idUsuariosConsulta = "";

			for (UsuarioDto usuarioDto : listUsuarios) {
				idUsuariosConsulta = idUsuariosConsulta + "'" + String.valueOf(usuarioDto.getIdUsuario()) + "',";
			}

			String ids = idUsuariosConsulta.substring(0, idUsuariosConsulta.length() - 1);
			List<Solicitud> listSolicitudes = jdbcTemplate.query(String
					.format("SELECT * FROM Solicitud WHERE idUsuarioRevisor IN (%s) and estado <> 'RESUELTA'", ids),
					new MapperSolicitud());

			int contador = 0;
			for (Solicitud solicitud : listSolicitudes) {
				if (!mapResult.containsKey(solicitud.getIdUsuarioRevisor())) {
					contador = 0;
					mapResult.put(solicitud.getIdUsuarioRevisor(), ++contador);

				} else {
					int numeroAsignaciones = mapResult.get(solicitud.getIdUsuarioRevisor());
					mapResult.replace(solicitud.getIdUsuarioRevisor(), ++numeroAsignaciones);
				}
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException) {

				throw GeneralException.throwException(this, ex,
						"Error estableciendo comunicación con la base de datos");
			}
			throw new BaseDatosException("Error consultando la capacidad de los revisores");
		}
		return mapResult;
	}

	public void actualizarSolicitudAsignada(int idSolicitud, String estado, String idRevisor) throws Exception {
		try {
			String updateQuery = "update Solicitud set  estado = ?, idUsuarioRevisor = ? where idSolicitud = ?";
			jdbcTemplate.update(updateQuery, estado, idRevisor, idSolicitud);
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo comunicación con la base de datos");
			}
			throw new BaseDatosException("Error actualizando la solicitud");
		}
	}

}
