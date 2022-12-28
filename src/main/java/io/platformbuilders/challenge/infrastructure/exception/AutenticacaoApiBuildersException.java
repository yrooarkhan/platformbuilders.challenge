package io.platformbuilders.challenge.infrastructure.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.platformbuilders.challenge.infrastructure.exception.template.ApiBuildersException;

public class AutenticacaoApiBuildersException extends ApiBuildersException {

	private static final long serialVersionUID = -8484932609977452838L;

	public AutenticacaoApiBuildersException() {
		super(UNAUTHORIZED,
				"Não foi possível se autenticar com a aplicação terceira API Builders com as credenciais informadas.");
	}

}