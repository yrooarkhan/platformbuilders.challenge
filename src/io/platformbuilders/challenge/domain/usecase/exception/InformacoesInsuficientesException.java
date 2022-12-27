package io.platformbuilders.challenge.domain.usecase.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

public class InformacoesInsuficientesException extends CalculadoraException {

	private static final String INFORMACOES_INSUFICIENTES = "O código de boleto (bar_code) e data de pagamento (payment_date) são obrigatórios para calcular o juros do boleto.";
	private static final long serialVersionUID = -315185706296497654L;

	public InformacoesInsuficientesException() {
		super(BAD_REQUEST, INFORMACOES_INSUFICIENTES);
	}

}