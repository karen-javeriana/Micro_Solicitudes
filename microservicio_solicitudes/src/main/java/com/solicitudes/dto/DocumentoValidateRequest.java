package com.solicitudes.dto;

public class DocumentoValidateRequest {
	private String image;

	public DocumentoValidateRequest() {
		super();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public DocumentoValidateRequest(String image) {
		super();
		this.image = image;
	}

}
