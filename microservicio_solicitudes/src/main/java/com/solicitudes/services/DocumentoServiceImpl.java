package com.solicitudes.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpServerErrorException.BadGateway;
import org.springframework.web.client.RestTemplate;

import com.excepciones.GeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoSecurityException;
import com.solicitudes.dao.IDocumentoDao;
import com.solicitudes.dto.DocumentoValidateRequest;
import com.solicitudes.dto.ErrorDto;
import com.solicitudes.model.Documento;

@Service
public class DocumentoServiceImpl implements IDocumentoService {

	@Autowired
	IDocumentoDao documentoDao;

	@Autowired
	RestTemplate template;

	private final MongoTemplate mongoTemplate;

	@Autowired
	public DocumentoServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public String crearDocumento(String cedula, String historiaClinica, String email, String tipoDoc, String numeroDoc)
			throws Exception {
		Documento documento = new Documento();
		try {
			documento.setCedula(cedula);
			documento.setHistoriaClinica(historiaClinica);
			documento.setNumeroDoc(numeroDoc);
			documento.setTipoDoc(tipoDoc);
			documento.setEmail(email);
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
		Documento documento = new Documento();
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			List<Documento> documentos = mongoTemplate.find(query, Documento.class);
			if (documentos != null && documentos.size() > 0) {
				documento = documentos.get(0);
			} else {
				throw GeneralException.throwException(this, new Exception(), "No hay documentos asociados al id",
						"VD01");
			}
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
		return documento;
	}

	@Override
	public Documento getDocumentoPorCriterios(String tipoIdentificacion, String numeroIdentificacion, String email)
			throws Exception {
		Documento documento = new Documento();
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("tipoDoc").is(tipoIdentificacion));
			query.addCriteria(Criteria.where("numeroDoc").is(numeroIdentificacion));
			query.addCriteria(Criteria.where("email").is(email));

			List<Documento> documentos = mongoTemplate.find(query, Documento.class);
			if (documentos != null && documentos.size() > 0) {
				documento = documentos.get(0);
			} else {
				documento = null;
			}
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
		return documento;
	}

	public ErrorDto setMessageExceptionRequest(Exception ex) {

		ErrorDto infoError;

		if (ex instanceof GeneralException) {
			infoError = ((GeneralException) ex).getCamposError();
		} else {
			infoError = new ErrorDto();
			infoError.setDescripcionError(ex.getMessage());

		}
		return infoError;
	}

	@Override
	public boolean validarDocumento(String cedula, String token) throws Exception {
		boolean isValid = false;
		try {

			String url = "https://sb-documents.mybluemix.net/api/v1/sb/document/";
			ObjectMapper mapper = new ObjectMapper();
			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			headers.add("Authorization", "Bearer " + token);

			DocumentoValidateRequest request = new DocumentoValidateRequest();
			request.setImage(cedula);
			String requestJson = new Gson().toJson(request);
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			String result = template.postForObject(url, entity, String.class);

			JsonNode resultNode = mapper.readTree(result);
			JsonNode document = resultNode.get("document");
			Double confidence = document.get("Confidence").asDouble();

			if (confidence >= 90.0) {
				isValid = true;
			} else {
				isValid = false;
			}

		} catch (Exception ex) {
			if (ex instanceof BadGateway || ex.getMessage().contains("An error has occurred processing your request")
					|| ex instanceof NotFound) {
				return false;
			}
			return false;
		}
		return isValid;
	}

}
