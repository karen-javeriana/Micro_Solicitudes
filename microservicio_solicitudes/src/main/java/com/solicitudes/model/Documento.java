package com.solicitudes.model;

import javax.persistence.Id;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Documento")
public class Documento {

	@Id
	private String id;

	@Field(name = "tipoDoc")
	private String tipoDoc;

	@Field(name = "numeroDoc")
	private String numeroDoc;

	@Field(name = "email")
	private String email;

	@Field(name = "cedula")
	private String cedula;

	@Field(name = "historiaClinica")
	private String historiaClinica;

	public Documento() {
		super();
	}

	@PersistenceConstructor
	public Documento(String id, String tipoDoc, String numeroDoc, String email, String cedula, String historiaClinica) {
		super();
		this.id = id;
		this.tipoDoc = tipoDoc;
		this.numeroDoc = numeroDoc;
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

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getNumeroDoc() {
		return numeroDoc;
	}

	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
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

}
