package com.solicitudes.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the Solicitud database table.
 * 
 */
@Entity
@Table(name = "Solicitud")
@NamedQuery(name = "Pago.findAll", query = "SELECT s FROM Solicitud s")
public class Solicitud implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2164967490469491766L;

	@Id
	private String id;

	private String idProducto;

	private String estado;

	private String descripcion;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaSolicitud;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaRevision;

	private String idDocumentosAdjuntos;

	private String idUsuarioRevisor;

	private String nombresCliente;

	private String apellidosCliente;

	private String numeroIdentificacion;

	private String tipoIdentificacion;

	private String email;

	private String foto;

	private String telefono;

	private String fechaNacimiento;

	private String ciudad;

	private String pais;

	private String idCliente;

	private String direccion;

	private String genero;

	private Double scoreSarlaft;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Solicitud() {
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

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
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

	public String getIdUsuarioRevisor() {
		return idUsuarioRevisor;
	}

	public void setIdUsuarioRevisor(String idUsuarioRevisor) {
		this.idUsuarioRevisor = idUsuarioRevisor;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getScoreSarlaft() {
		return scoreSarlaft;
	}

	public void setScoreSarlaft(Double scoreSarlaft) {
		this.scoreSarlaft = scoreSarlaft;
	}
}