package io.platformbuilders.challenge.infrastructure.web;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.platformbuilders.challenge.domain.model.FalhaProcessamento;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

class CalculadoraDeJurosExceptionHandlerTest {

	private static final String ERRO_DESCONHECIDO = "Um erro inesperado ocorreu. Por favor, tente novamente. Se o erro persistir, entre em contato com nosso suporte.";
	private static final String ERRO_CONHECIDO = "Exemplo de mensagem de erro.";

	private static CalculadoraDeJurosExceptionHandler exceptionHandler;

	@BeforeAll
	static void criaInstanciaExceptionHandler() {
		CalculadoraDeJurosExceptionHandlerTest.exceptionHandler = new CalculadoraDeJurosExceptionHandler();
	}

	@Test
	void deveExibirMensagemDoErro_casoSejaUmaInstanciaDeBuildersPayException_quandoTratandoErrosConhecidos() {
		CalculadoraException excecao = Mockito.mock(CalculadoraException.class);
		Mockito.doReturn(UNPROCESSABLE_ENTITY).when(excecao).getStatusHttp();
		Mockito.doReturn(ERRO_CONHECIDO).when(excecao).getMessage();

		FalhaProcessamento falha = exceptionHandler.trataErrosConhecidos(excecao).getBody();
		Assertions.assertEquals(UNPROCESSABLE_ENTITY.value(), falha.getCodigoErro());
		Assertions.assertEquals(ERRO_CONHECIDO, falha.getDescricao());
	}

	@Test
	void deveExibirMensagemGenerica_casoSejaUmaInstanciaDeBuildersPayException_quandoTratandoErrosConhecidos() {
		Exception excecao = Mockito.mock(Exception.class);
		Mockito.doReturn(ERRO_CONHECIDO).when(excecao).getMessage();

		FalhaProcessamento falha = exceptionHandler.trataErrosDesconhecidos(excecao).getBody();
		Assertions.assertEquals(INTERNAL_SERVER_ERROR.value(), falha.getCodigoErro());
		Assertions.assertEquals(ERRO_DESCONHECIDO, falha.getDescricao());
	}

}