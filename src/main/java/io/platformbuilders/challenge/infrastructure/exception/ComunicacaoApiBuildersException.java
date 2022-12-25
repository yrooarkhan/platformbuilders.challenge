package io.platformbuilders.challenge.infrastructure.exception;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import io.platformbuilders.challenge.infrastructure.exception.template.ApiBuildersException;

public class ComunicacaoApiBuildersException extends ApiBuildersException {

	private static final long serialVersionUID = -508419984176994333L;

	public ComunicacaoApiBuildersException() {
		super(SERVICE_UNAVAILABLE, "Não foi possível se comunicar com a aplicação terceira API Builders.");
	}

}