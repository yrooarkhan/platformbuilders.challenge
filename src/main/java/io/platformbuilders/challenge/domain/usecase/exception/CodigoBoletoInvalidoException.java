package io.platformbuilders.challenge.domain.usecase.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

public class CodigoBoletoInvalidoException extends CalculadoraException {

	private static final long serialVersionUID = 170379174994216098L;

	public CodigoBoletoInvalidoException() {
		super(BAD_REQUEST, "O código de boleto informado não pertence nenhum boleto da nossa base de dados.");
	}

}
