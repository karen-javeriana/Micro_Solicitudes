package com.solicitudes.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excepciones.GeneralException;
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
		Documento documento = new Documento();
		try {

			documento.setCedula(cedula);
			documento.setHistoriaClinica(historiaClinica);
			documento.setNombreCliente(nombreCliente);
			documentoDao.save(documento);

		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo",
						"BD01");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos", "BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
		return documento.getId();
	}

	@Override
	public void deleteDocumento(String id) throws Exception {
		try {
			documentoDao.deleteById(id);
		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo",
						"BD01");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos", "BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
	}

	@Override
	public Documento getDocumentoPorId(String id) throws Exception {
		Optional<Documento> documento = null;
		try {
//			documento = documentoDao.findById(id);
//
//			if (documento == null || documento.isEmpty()) {
//				throw GeneralException.throwException(this, new Exception(), "No hay documentos asociados al id", "VD01");
//			}

		} catch (Exception ex) {
			if (ex instanceof MongoSecurityException) {
				throw GeneralException.throwException(this, ex, "Error de autenciación con la base de datos Mongo",
						"BD01");
			} else if (ex instanceof MongoExecutionTimeoutException) {
				throw GeneralException.throwException(this, ex,
						"Error estableciendo la comunicación con la base de datos", "BD01");
			}
			throw GeneralException.throwException(this, ex);
		}
		return null;
	}

}
