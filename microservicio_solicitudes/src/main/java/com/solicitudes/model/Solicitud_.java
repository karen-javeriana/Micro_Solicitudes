package com.solicitudes.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-11-23T18:20:35.963-0500")
@StaticMetamodel(Solicitud.class)
public class Solicitud_ {
	public static volatile SingularAttribute<Solicitud, String> id;
	public static volatile SingularAttribute<Solicitud, String> idProducto;
	public static volatile SingularAttribute<Solicitud, String> estado;
	public static volatile SingularAttribute<Solicitud, String> descripcion;
	public static volatile SingularAttribute<Solicitud, Date> fechaSolicitud;
	public static volatile SingularAttribute<Solicitud, Date> fechaRevision;
	public static volatile SingularAttribute<Solicitud, String> idDocumentosAdjuntos;
	public static volatile SingularAttribute<Solicitud, String> idUsuarioRevisor;
	public static volatile SingularAttribute<Solicitud, String> nombresCliente;
	public static volatile SingularAttribute<Solicitud, String> apellidosCliente;
	public static volatile SingularAttribute<Solicitud, String> numeroIdentificacion;
	public static volatile SingularAttribute<Solicitud, String> tipoIdentificacion;
	public static volatile SingularAttribute<Solicitud, String> email;
	public static volatile SingularAttribute<Solicitud, String> foto;
	public static volatile SingularAttribute<Solicitud, String> telefono;
	public static volatile SingularAttribute<Solicitud, String> fechaNacimiento;
	public static volatile SingularAttribute<Solicitud, String> ciudad;
	public static volatile SingularAttribute<Solicitud, String> pais;
	public static volatile SingularAttribute<Solicitud, String> idCliente;
	public static volatile SingularAttribute<Solicitud, String> direccion;
	public static volatile SingularAttribute<Solicitud, String> genero;
	public static volatile SingularAttribute<Solicitud, Double> scoreSarlaft;
}
