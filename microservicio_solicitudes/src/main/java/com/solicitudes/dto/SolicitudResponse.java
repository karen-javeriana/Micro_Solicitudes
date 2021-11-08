package com.solicitudes.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.solicitudes.model.Documento;

public class SolicitudResponse {

	@XmlElement(name = "solicitud", required = false)
	private SolicitudRequest solicitud;

	@XmlElement(name = "error", required = false)
	private String error;

	@XmlElement(name = "error", required = false)
	private Boolean sucess;

	@XmlElement(name = "listSolicitudes", required = false)
	private List<SolicitudRequest> listSolicitudes;

	private Documento documento;

	public SolicitudResponse() {
		super();
	}

	public SolicitudRequest getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudRequest solicitud) {
		this.solicitud = solicitud;
	}

	public List<SolicitudRequest> getListSolicitudes() {
		return listSolicitudes;
	}

	public void setListSolicitudes(List<SolicitudRequest> listSolicitudes) {
		this.listSolicitudes = listSolicitudes;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
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

}
