package io.platformbuilders.challenge.infrastructure.web.v1;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.platformbuilders.challenge.domain.model.FalhaProcessamento;
import io.platformbuilders.challenge.infrastructure.exception.BuildersPayException;

@ControllerAdvice(basePackageClasses = BuildersPayController.class)
public class BuildersPayExceptionHandler {

	private static final String ERRO_INESPERADO = "Um erro inesperado ocorreu. Por favor, tente novamente. Se o erro persistir, entre em contato com nosso suporte.";

	@ExceptionHandler(BuildersPayException.class)
	public ResponseEntity<FalhaProcessamento> trataErrosConhecidos(BuildersPayException excecao) {
		FalhaProcessamento falhaCalculo = new FalhaProcessamento(excecao.getStatusHttp(), excecao.getMessage());
		return new ResponseEntity<>(falhaCalculo, excecao.getStatusHttp());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<FalhaProcessamento> trataErrosDesconhecidos(Exception excecao) {
		FalhaProcessamento falhaProcessamento = new FalhaProcessamento(INTERNAL_SERVER_ERROR, ERRO_INESPERADO);
		return new ResponseEntity<>(falhaProcessamento, INTERNAL_SERVER_ERROR);
	}

}
