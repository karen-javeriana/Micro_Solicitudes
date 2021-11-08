package com.excepciones;

public class ValidacionDatosException extends Exception {

	private static final long serialVersionUID = -5899059020516801145L;
	private String detail;

	public ValidacionDatosException(String message) {
		super(message);
	}

	public ValidacionDatosException(String message, String detail) {
		super(message);
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
