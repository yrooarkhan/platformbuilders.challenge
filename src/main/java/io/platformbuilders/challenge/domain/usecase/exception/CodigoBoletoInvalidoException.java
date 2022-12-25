package io.platformbuilders.challenge.domain.usecase.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import io.platformbuilders.challenge.infrastructure.expected.BuildersPayException;

public class CodigoBoletoInvalidoException extends BuildersPayException {

	private static final long serialVersionUID = 170379174994216098L;

	public CodigoBoletoInvalidoException() {
		super(UNPROCESSABLE_ENTITY, "O código de boleto informado não pertence nenhum boleto da nossa base de dados.");
	}

}
