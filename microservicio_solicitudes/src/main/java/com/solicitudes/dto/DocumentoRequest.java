package com.solicitudes.dto;

public class DocumentoRequest {

	private String id;
	
	private String cedula;

	private String historiaClinica;

	private String email;

	public DocumentoRequest() {
		super();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DocumentoRequest(String cedula, String historiaClinica, String id, String email) {
		super();
		this.cedula = cedula;
		this.historiaClinica = historiaClinica;
		this.id = id;
		this.email = email;
	}

}
