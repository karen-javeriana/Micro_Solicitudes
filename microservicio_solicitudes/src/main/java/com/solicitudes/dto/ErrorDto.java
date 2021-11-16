package com.solicitudes.dto;


public class ErrorDto {

	private String descripcionError;
	
	private String codeError;

	public ErrorDto() {
		super();
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public String getCodeError() {
		return codeError;
	}

	public void setCodeError(String codeError) {
		this.codeError = codeError;
	}

}
