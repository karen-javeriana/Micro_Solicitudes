package com.solicitudes.model;

import javax.persistence.Id;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Documento")
public class Documento {

	@Id
	private String id;

	@Field(name = "nombreCliente")
	private String nombreCliente;

	@Field(name = "cedula")
	private String cedula;

	@Field(name = "historiaClinica")
	private String historiaClinica;

	public Documento() {
		super();
	}

	@PersistenceConstructor
	public Documento(String id, String nombreCliente, String cedula, String historiaClinica) {
		super();
		this.id = id;
		this.nombreCliente = nombreCliente;
		this.cedula = cedula;
		this.historiaClinica = historiaClinica;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
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
