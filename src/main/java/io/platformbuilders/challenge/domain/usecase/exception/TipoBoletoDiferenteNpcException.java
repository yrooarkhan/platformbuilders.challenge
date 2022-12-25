package io.platformbuilders.challenge.domain.usecase.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import io.platformbuilders.challenge.infrastructure.exception.BuildersPayException;

public class TipoBoletoDiferenteNpcException extends BuildersPayException {

	private static final long serialVersionUID = -315185706296497654L;

	public TipoBoletoDiferenteNpcException() {
		super(UNPROCESSABLE_ENTITY, "O código de boleto informado não pertence a um boleto do tipo NPC.");
	}

}
