package com.excepciones;

import com.solicitudes.dto.ErrorDto;

public class GeneralException extends Exception {

	private static final long serialVersionUID = 8318381692074327024L;

	private String mensajeUsuario;
	private ErrorDto camposError;

	public GeneralException(ErrorDto camposError, Throwable cause) {
		super(camposError.getDescripcionError(), cause);
		this.camposError = camposError;
	}

	public static Exception throwException(Object classE, Exception ex, String description) {
		if (ex instanceof GeneralException) {
			return ex;
		}

		ErrorDto infoError = new ErrorDto();
		infoError.setDescripcionError(description);

		return new GeneralException(infoError, ex.getCause());

	}

	public static Exception throwException(Object classE, Exception ex) {

		if (ex instanceof GeneralException) {
			return ex;
		}

		ErrorDto infoError = new ErrorDto();
		infoError.setDescripcionError(ex.getMessage());

		return new GeneralException(infoError, ex.getCause());

	}

	public String getMensajeUsuario() {
		return mensajeUsuario;
	}

	public void setMensajeUsuario(String mensajeUsuario) {
		this.mensajeUsuario = mensajeUsuario;
	}

	public ErrorDto getCamposError() {
		return camposError;
	}

	public void setCamposError(ErrorDto camposError) {
		this.camposError = camposError;
	}

}
