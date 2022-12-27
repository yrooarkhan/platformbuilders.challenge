package io.platformbuilders.challenge.infrastructure.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.platformbuilders.challenge.domain.model.FalhaProcessamento;
import io.platformbuilders.challenge.infrastructure.exception.template.ApiBuildersException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;
import io.platformbuilders.challenge.infrastructure.exception.template.ErroConhecido;

@ControllerAdvice(basePackageClasses = CalculadoraDeJurosController.class)
public class CalculadoraDeJurosExceptionHandler {

	private static final String FORMATO_INVALIDO = "Um ou mais campos informados estão em um formato inválido. Por favor, verifique a documentação e tente novamente.";
	private static final String ERRO_INESPERADO = "Um erro inesperado ocorreu. Por favor, tente novamente. Se o erro persistir, entre em contato com nosso suporte.";

	@ExceptionHandler({ CalculadoraException.class, ApiBuildersException.class })
	public ResponseEntity<FalhaProcessamento> trataErrosConhecidos(ErroConhecido excecao) {
		FalhaProcessamento falhaCalculo = new FalhaProcessamento(excecao.getStatusHttp(), excecao.getMessage());
		return new ResponseEntity<>(falhaCalculo, excecao.getStatusHttp());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<FalhaProcessamento> trataErrosDesconhecidos(HttpMessageNotReadableException excecao) {
		FalhaProcessamento falhaProcessamento = new FalhaProcessamento(BAD_REQUEST, FORMATO_INVALIDO);
		return new ResponseEntity<>(falhaProcessamento, BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<FalhaProcessamento> trataErrosDesconhecidos(Exception excecao) {
		FalhaProcessamento falhaProcessamento = new FalhaProcessamento(INTERNAL_SERVER_ERROR, ERRO_INESPERADO);
		return new ResponseEntity<>(falhaProcessamento, INTERNAL_SERVER_ERROR);
	}

}
