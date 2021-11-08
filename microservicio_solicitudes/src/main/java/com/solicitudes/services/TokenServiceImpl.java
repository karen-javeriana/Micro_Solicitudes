package com.solicitudes.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.excepciones.GeneralException;
@Service
public class TokenServiceImpl implements ITokenService {

	@Value("${jwt.secret}")
	private String claveSecreta;

	public boolean isTokenValid(String token) throws Exception {
		try {
			Algorithm algorithm = Algorithm.HMAC256(claveSecreta);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (Exception e) {
			
			if(e instanceof TokenExpiredException) {
				throw GeneralException.throwException(this, e,
						"El token ya expiro");
			}
			if(e instanceof SignatureVerificationException) {
				throw GeneralException.throwException(this, e,
						"la firma del token no es válida");
			}
		}
		return false;
	}

}
