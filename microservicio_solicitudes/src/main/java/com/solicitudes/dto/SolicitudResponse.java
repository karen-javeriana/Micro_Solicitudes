package com.solicitudes.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SolicitudResponse {

	@XmlElement(name = "solicitud", required = false)
	private SolicitudRequest solicitud;
	
	@XmlElement(name = "pages", required = false)
	private Integer pages;

	@XmlElement(name = "error", required = false)
	private String error;

	@XmlElement(name = "error", required = false)
	private Boolean sucess;

	@XmlElement(name = "listSolicitudes", required = false)
	private List<SolicitudRequest> listSolicitudes;

	public SolicitudResponse(SolicitudRequest solicitud, String error, Boolean sucess,
			List<SolicitudRequest> listSolicitudes, Integer pages) {
		super();
		this.solicitud = solicitud;
		this.error = error;
		this.sucess = sucess;
		this.listSolicitudes = listSolicitudes;
		this.pages=pages;
	}
	

	public SolicitudResponse(SolicitudRequest solicitud, String error, Boolean sucess,
			List<SolicitudRequest> listSolicitudes) {
		super();
		this.solicitud = solicitud;
		this.error = error;
		this.sucess = sucess;
		this.listSolicitudes = listSolicitudes;
	}


	public SolicitudResponse() {
		super();
	}

	public SolicitudResponse(String error, Boolean sucess) {
		super();
		this.error = error;
		this.sucess = sucess;
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


	public Integer getPages() {
		return pages;
	}


	public void setPages(Integer pages) {
		this.pages = pages;
	}

	
}
