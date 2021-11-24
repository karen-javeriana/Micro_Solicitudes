package com.solicitudes.dto;

import javax.xml.bind.annotation.XmlElement;

import com.solicitudes.model.Documento;

public class DocumentoResponse {

	private Documento documento;

	@XmlElement(name = "Name", required = false)
	private String name;

	@XmlElement(name = "Confidence", required = false)
	private Double confidence;
	
	@XmlElement(name = "error", required = false)
	private String error;

	@XmlElement(name = "error", required = false)
	private Boolean sucess;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Boolean getSucess() {
		return sucess;
	}

	public void setSucess(Boolean sucess) {
		this.sucess = sucess;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public DocumentoResponse(Documento documento, String name, Double confidence, String error, Boolean sucess) {
		super();
		this.documento = documento;
		this.name = name;
		this.confidence = confidence;
		this.error = error;
		this.sucess = sucess;
	}

	public DocumentoResponse(String error, Boolean sucess) {
		super();
		this.error = error;
		this.sucess = sucess;
	}

}
