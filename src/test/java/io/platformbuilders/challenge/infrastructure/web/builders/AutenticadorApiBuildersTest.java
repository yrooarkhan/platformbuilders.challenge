package io.platformbuilders.challenge.infrastructure.web.builders;

import static java.time.LocalDateTime.of;
import static java.time.Month.DECEMBER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Stubber;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

import io.platformbuilders.challenge.infrastructure.exception.AutenticacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.AutenticadorApiBuildersProvider;

class AutenticadorApiBuildersTest {

	private static final int SEIS_HORAS = 18;
	private static final int CINQUENTA_MIN = 50;
	private static final int CINQUENTA_SEIS_MIN = 55;

	private static final String STRING_VAZIA = "";
	private static final String CHAVE_AUTENTICACAO_01 = "8cd6e6f5-f5e6-48ed-8c55-934a2a1e38d9";
	private static final String CHAVE_AUTENTICACAO_02 = "0e482695-1c8c-43f3-9ab9-799702ac92d6";
	private static final String RESPOSTA_01 = "{ 'token': '8cd6e6f5-f5e6-48ed-8c55-934a2a1e38d9', 'expires_in': '2022-12-25T18:53:00.372102' }";
	private static final String RESPOSTA_02 = "{ 'token': '0e482695-1c8c-43f3-9ab9-799702ac92d6', 'expires_in': '2022-12-25T19:00:00.372102' }";

	private static final LocalDateTime ANTES_EXPIRACAO = of(2022, DECEMBER, 25, SEIS_HORAS, CINQUENTA_MIN);
	private static final LocalDateTime DEPOIS_EXPIRACAO = of(2022, DECEMBER, 25, SEIS_HORAS, CINQUENTA_SEIS_MIN);

	private AutenticadorApiBuilders autenticador;
	private AutenticadorApiBuildersProvider autenticadorProvider;

	@BeforeEach
	void instanciaAutenticadorComMocks() {
		this.autenticadorProvider = Mockito.mock(AutenticadorApiBuildersProvider.class);
		Mockito.doReturn(STRING_VAZIA).when(autenticadorProvider).getConfiguracao();

		this.autenticador = new AutenticadorApiBuilders(autenticadorProvider);
	}

	@Test
	void deveRetornarNovaChaveAutenticacao_casoNaoPossuaChaveSalva_quandoAutenticando() throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.CREATED, new JsonNode(RESPOSTA_01));

		String chaveAutenticacao = autenticador.autentica();

		Assertions.assertEquals(CHAVE_AUTENTICACAO_01, chaveAutenticacao);
	}

	@Test
	void deveRetornarAMesmaChaveDeAutenticacao_casoPossuaChaveNaoExpirada_quandoAutenticando() throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.CREATED, new JsonNode(RESPOSTA_01));
		Mockito.doReturn(ANTES_EXPIRACAO).when(autenticadorProvider).now();

		String chaveAutenticacao = autenticador.autentica();
		String segundaChaveAutenticacao = autenticador.autentica();

		Assertions.assertEquals(chaveAutenticacao, segundaChaveAutenticacao);
		Mockito.verify(autenticadorProvider, times(1)).post(Mockito.anyString());
	}

	@Test
	void deveRetornarNovaChaveAutenticacao_casoPossuaChaveExpirada_quandoAutenticando() throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.CREATED, new JsonNode(RESPOSTA_01), new JsonNode(RESPOSTA_02));
		Mockito.doReturn(DEPOIS_EXPIRACAO).when(autenticadorProvider).now();

		String chaveAutenticacao = autenticador.autentica();
		String segundaChaveAutenticacao = autenticador.autentica();
		String terceiraChaveAutenticacao = autenticador.autentica();

		Assertions.assertEquals(CHAVE_AUTENTICACAO_01, chaveAutenticacao);
		Assertions.assertEquals(CHAVE_AUTENTICACAO_02, segundaChaveAutenticacao);
		Assertions.assertEquals(segundaChaveAutenticacao, terceiraChaveAutenticacao);
		Mockito.verify(autenticadorProvider, times(2)).post(Mockito.anyString());
	}

	@Test
	void deveEstourarExcecaoDeAutenticacao_casoHttpStatusDeRetornoSeja401_quandoAutenticandoComBaseNaConfiguracao()
			throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.UNAUTHORIZED, new JsonNode(null));
		Assertions.assertThrows(AutenticacaoApiBuildersException.class, () -> autenticador.autentica());
	}

	@Test
	void deveEstourarExcecaoDeComunicacao_casoHttpStatusDeRetornoSejaDiferenteDe201E401_quandoAutenticandoComBaseNaConfiguracao()
			throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.BAD_REQUEST, new JsonNode(null));
		Assertions.assertThrows(ComunicacaoApiBuildersException.class, () -> autenticador.autentica());
	}

	@Test
	void deveEstourarExcecaoDeComunicacao_casoHajaAlgumaFalhaAoFazerPost_quandoAutenticandoComBaseNaConfiguracao()
			throws UnirestException {
		mockErroAplicacaoTerceira();
		Assertions.assertThrows(ComunicacaoApiBuildersException.class, () -> autenticador.autentica());
	}

	private void mockErroAplicacaoTerceira() throws UnirestException {
		RequestBodyEntity bodyEntity = Mockito.mock(RequestBodyEntity.class);
		Mockito.doThrow(UnirestException.class).when(bodyEntity).asJson();

		HttpRequestWithBody requestWithBody = Mockito.mock(HttpRequestWithBody.class);
		Mockito.doReturn(requestWithBody).when(requestWithBody).header(anyString(), anyString());
		Mockito.doReturn(bodyEntity).when(requestWithBody).body(anyString());

		Mockito.doReturn(requestWithBody).when(autenticadorProvider).post(anyString());

	}

	@SuppressWarnings("unchecked")
	private void mockRespostaAplicacaoTerceira(HttpStatusCode statusCode, JsonNode... corposDeResposta)
			throws UnirestException {
		HttpResponse<JsonNode> reponse = Mockito.mock(HttpResponse.class);
		Mockito.doReturn(statusCode.value()).when(reponse).getStatus();

		Stubber retornos = Mockito.doReturn(corposDeResposta[0]);
		IntStream.range(1, corposDeResposta.length).forEach(i -> retornos.doReturn(corposDeResposta[i]));
		retornos.when(reponse).getBody();

		RequestBodyEntity bodyEntity = Mockito.mock(RequestBodyEntity.class);
		Mockito.doReturn(reponse).when(bodyEntity).asJson();

		HttpRequestWithBody requestWithBody = Mockito.mock(HttpRequestWithBody.class);
		Mockito.doReturn(requestWithBody).when(requestWithBody).header(anyString(), anyString());
		Mockito.doReturn(bodyEntity).when(requestWithBody).body(anyString());

		Mockito.doReturn(requestWithBody).when(autenticadorProvider).post(anyString());

	}

}