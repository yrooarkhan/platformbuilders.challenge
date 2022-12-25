package io.platformbuilders.challenge.domain.usecase.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import io.platformbuilders.challenge.infrastructure.exception.template.BoletoException;

public class BoletoNaoVencidoException extends BoletoException {

	private static final long serialVersionUID = -8473519203758429402L;

	public BoletoNaoVencidoException() {
		super(UNPROCESSABLE_ENTITY, "O código de boleto informado não pertence a um boleto vencido.");
	}

}
