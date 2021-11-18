package com.solicitudes.dto;

public class DocumentoRequest {
	private String cedula;

	private String historiaClinica;

	private String tipoIdentificacion;

	private String numeroIdentificacion;

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

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DocumentoRequest(String cedula, String historiaClinica, String tipoIdentificacion, String numeroIdentificacion,
			String email) {
		super();
		this.cedula = cedula;
		this.historiaClinica = historiaClinica;
		this.tipoIdentificacion = tipoIdentificacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.email = email;
	}

}
