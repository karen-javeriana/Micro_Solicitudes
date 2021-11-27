package com.solicitudes.model;

import javax.persistence.Id;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Documento")
public class Documento {

	@Id
	private String id;

	@Field(name = "id")
	private String idGenerado;

	@Field(name = "email")
	private String email;

	@Field(name = "cedula")
	private String cedula;

	@Field(name = "historiaClinica")
	private String historiaClinica;
	
	@Field(name = "estado")
	private String estado;

	public Documento() {
		super();
	}

	@PersistenceConstructor
	public Documento(String id, String idGenerado, String email, String cedula, String historiaClinica) {
		super();
		this.id = id;
		this.idGenerado = idGenerado;
		this.email = email;
		this.cedula = cedula;
		this.historiaClinica = historiaClinica;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdGenerado() {
		return idGenerado;
	}

	public void setIdGenerado(String idGenerado) {
		this.idGenerado = idGenerado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getHistoriaClinica() {
		return historiaClinica;
	}

	public void setHistoriaClinica(String historiaClinica) {
		this.historiaClinica = historiaClinica;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
