package com.solicitudes.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excepciones.GeneralException;
import com.excepciones.ValidacionDatosException;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoSecurityException;
import com.solicitudes.dao.IDocumentoDao;
import com.solicitudes.model.Documento;

@Service
public class MongoServiceImpl implements IMongoService {

	@Autowired
	IDocumentoDao documentoDao;

	@Override
	public String crearDocumento(String cedula, String historiaClinica, String nombreCliente) throws Exception {
		try {
			Documento documento = new Documento();
			documento.setCedula(cedula);
			documento.setHistoriaClinica(historiaClinica);
			documento.setNombreCliente(nombreCliente);
			documentoDao.save(documento);

			if (documento.getId() == null || documento.getId().isEmpty()) {
				throw new ValidacionDatosException("Ocurrio un error almacenando los documentos en la fuente de datos");
			}
			return documento.getId();
		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	@Override
	public void deleteDocumento(String id) throws Exception {
		try {
			documentoDao.deleteById(id);
		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	@Override
	public Documento getDocumentoPorId(String id) throws Exception {
		Optional<Documento> documento = null;
		try {
			documento = documentoDao.findById(id);

			if (documento == null) {
				throw new ValidacionDatosException("No hay documentos asociados al id");
			}

		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos");
			}
			throw GeneralException.throwException(this, ex);
		}
		return documento.get();
	}

}
