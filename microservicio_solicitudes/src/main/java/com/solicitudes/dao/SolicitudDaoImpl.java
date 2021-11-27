package com.solicitudes.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.CommunicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import com.excepciones.GeneralException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.solicitudes.dto.UsuarioDto;
import com.solicitudes.model.Solicitud;

@Repository
public class SolicitudDaoImpl implements ISolicitudDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Solicitud> obtenerSolicitudPorIdUsuarioRevisor(String idUsuarioRevisor, Integer page) throws Exception {
		try {
			if (page == null || page == 0) {
				throw GeneralException.throwException(this, new Exception(),
						"Es obligatorio el parametro {page} para consultar un rango de solicitudes", "VD02");
			} else {

				List<Solicitud> solicitudes = jdbcTemplate.query(
						"select * from Solicitud WHERE idUsuarioRevisor =? and estado ='ASIGNADA' limit ?,10 ",
						new MapperSolicitud(), idUsuarioRevisor, (page - 1) * 10);

				if (solicitudes.size() > 0) {
					return solicitudes;
				} else {
					throw GeneralException.throwException(this, new Exception(),
							"El revisor no tiene solicitudes asignadas", "VD01");
				}
			}

		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {
				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	public void crearSolicitud(Solicitud solicitud) throws Exception {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
				.withTableName("Solicitud");
		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("id", solicitud.getId());
			parameters.put("idProducto", solicitud.getIdProducto());
			parameters.put("estado", solicitud.getEstado() == null ? "SIN ASIGNAR" : solicitud.getEstado());
			parameters.put("descripcion", solicitud.getDescripcion());
			parameters.put("idDocumentosAdjuntos", solicitud.getIdDocumentosAdjuntos());
			parameters.put("fechaSolicitud", new Date());
			parameters.put("idUsuarioRevisor", solicitud.getIdUsuarioRevisor());
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
			parameters.put("scoreSarlaft", solicitud.getScoreSarlaft() == null ? 0.0 : solicitud.getScoreSarlaft());

			simpleJdbcInsert.execute(parameters);
		} catch (Exception ex) {
			if (ex instanceof DataAccessResourceFailureException || ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {
				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	public void actualizarSolicitud(Solicitud solicitud, String id, String estado) throws Exception {
		try {

			if (solicitud.getIdDocumentosAdjuntos() != null) {
				String updateQuery = "update Solicitud set fechaRevision = ? , estado = ? , idDocumentosAdjuntos = ? where id = ?";
				jdbcTemplate.update(updateQuery, new Date(), estado, solicitud.getIdDocumentosAdjuntos(), id);
			}else {
				String updateQuery = "update Solicitud set fechaRevision = ? , estado = ? where id = ?";
				jdbcTemplate.update(updateQuery, new Date(), estado, id);
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException || ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {
				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
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
			List<Solicitud> listSolicitudes = jdbcTemplate.query(String.format(
					"SELECT * FROM Solicitud WHERE idUsuarioRevisor IN (%s) and estado <> 'RESUELTA' and estado <> 'RECHAZADA'",
					ids), new MapperSolicitud());

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
			if (ex.getCause() instanceof CommunicationsException || ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {

				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
		}
		return mapResult;
	}

	public void actualizarSolicitudAsignada(String idSolicitud, String estado, String idRevisor) throws Exception {
		try {
			String updateQuery = "update Solicitud set  estado = ?, idUsuarioRevisor = ? where id = ?";
			jdbcTemplate.update(updateQuery, estado, idRevisor, idSolicitud);
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException || ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {
				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
		}
	}

	public Solicitud obtenerSolicitudPorId(String id) throws Exception {
		try {
			List<Solicitud> solicitudes = jdbcTemplate.query("select * from Solicitud WHERE id = ? ",
					new MapperSolicitud(), id);

			if (solicitudes != null && solicitudes.size() > 0) {
				return solicitudes.get(0);
			} else {
				return null;
			}
		} catch (Exception ex) {
			if (ex.getCause() instanceof CommunicationsException
					|| ex.getCause() instanceof CannotGetJdbcConnectionException) {
				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	@SuppressWarnings("deprecation")
	public int obtenerPaginacionSolicitudes(String estadoSolicitud) throws Exception {
		double totalRows = 0;
		int pages = 0;
		try {
			if (estadoSolicitud != null) {
				totalRows = jdbcTemplate.queryForObject("select count(*) from Solicitud  where estado = ?",
						new Object[] { estadoSolicitud }, Double.class);
				totalRows = totalRows / 10;
			} else {
				totalRows = jdbcTemplate.queryForObject("select count(*) from Solicitud  where estado ='ASIGNADA'",
						new Object[] { estadoSolicitud }, Double.class);
				totalRows = totalRows / 10;
			}

		} catch (Exception ex) {

			if (ex.getCause() instanceof CommunicationException) {

				throw GeneralException.throwException(this, ex, "Error estableciendo comunicación con la base de datos",
						"AUT01");
			}
			throw GeneralException.throwException(this, ex);
		}
		pages = (int) Math.ceil((totalRows));
		return pages;
	}

}
