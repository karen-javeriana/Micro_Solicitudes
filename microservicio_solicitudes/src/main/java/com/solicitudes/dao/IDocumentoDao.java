package com.solicitudes.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.solicitudes.model.Documento;

@Repository
public interface IDocumentoDao extends MongoRepository<Documento, String> {


}
