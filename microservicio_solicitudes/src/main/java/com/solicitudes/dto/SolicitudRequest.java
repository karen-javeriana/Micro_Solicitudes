package com.solicitudes.dto;

import java.util.Date;

public class SolicitudRequest {
	private int idSolicitud;
	private String idProducto;
	private String estado;
	private String descripcion;
	private Date fechaSolicitud;
	private Date fechaRevision;
	private String idDocumentosAdjuntos;
	private String idUsuarioRevisor;
	private String nombresCliente;
	private String apellidosCliente;
	private String numeroIdentificacion;
	private String tipoIdentificacion;
	private String email;
	private String cedulaAdjunta;
	private String historiaClinicaAdjunta;
	private String foto;
	private String telefono;
	private String fechaNacimiento;
	private String ciudad;
	private String pais;
	private String direccion;
	private String genero;
	private String idCliente;

	public SolicitudRequest() {
		super();
	}

	public SolicitudRequest(int idSolicitud, String idProducto, String estado, String descripcion, Date fechaSolicitud,
			Date fechaRevision, String idDocumentosAdjuntos, String idUsuarioRevisor, String nombresCliente,
			String apellidosCliente, String numeroIdentificacion, String tipoIdentificacion, String email,
			String cedulaAdjunta, String historiaClinicaAdjunta, String foto, String telefono, String fechaNacimiento,
			String ciudad, String pais, String direccion, String genero, String idCliente) {
		super();
		this.idSolicitud = idSolicitud;
		this.idProducto = idProducto;
		this.estado = estado;
		this.descripcion = descripcion;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaRevision = fechaRevision;
		this.idDocumentosAdjuntos = idDocumentosAdjuntos;
		this.idUsuarioRevisor = idUsuarioRevisor;
		this.nombresCliente = nombresCliente;
		this.apellidosCliente = apellidosCliente;
		this.numeroIdentificacion = numeroIdentificacion;
		this.tipoIdentificacion = tipoIdentificacion;
		this.email = email;
		this.cedulaAdjunta = cedulaAdjunta;
		this.historiaClinicaAdjunta = historiaClinicaAdjunta;
		this.foto = foto;
		this.telefono = telefono;
		this.fechaNacimiento = fechaNacimiento;
		this.ciudad = ciudad;
		this.pais = pais;
		this.direccion = direccion;
		this.genero = genero;
		this.idCliente = idCliente;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public String getIdUsuarioRevisor() {
		return idUsuarioRevisor;
	}

	public void setIdUsuarioRevisor(String idUsuarioRevisor) {
		this.idUsuarioRevisor = idUsuarioRevisor;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getIdDocumentosAdjuntos() {
		return idDocumentosAdjuntos;
	}

	public void setIdDocumentosAdjuntos(String idDocumentosAdjuntos) {
		this.idDocumentosAdjuntos = idDocumentosAdjuntos;
	}

	public String getNombresCliente() {
		return nombresCliente;
	}

	public void setNombresCliente(String nombresCliente) {
		this.nombresCliente = nombresCliente;
	}

	public String getApellidosCliente() {
		return apellidosCliente;
	}

	public void setApellidosCliente(String apellidosCliente) {
		this.apellidosCliente = apellidosCliente;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCedulaAdjunta() {
		return cedulaAdjunta;
	}

	public void setCedulaAdjunta(String cedulaAdjunta) {
		this.cedulaAdjunta = cedulaAdjunta;
	}

	public String getHistoriaClinicaAdjunta() {
		return historiaClinicaAdjunta;
	}

	public void setHistoriaClinicaAdjunta(String historiaClinicaAdjunta) {
		this.historiaClinicaAdjunta = historiaClinicaAdjunta;
	}

}
