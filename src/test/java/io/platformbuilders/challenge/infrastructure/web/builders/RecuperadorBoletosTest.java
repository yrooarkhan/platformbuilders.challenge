package io.platformbuilders.challenge.infrastructure.web.builders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.usecase.exception.CodigoBoletoInvalidoException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.BuildersApiProvider;

class RecuperadorBoletosTest {

	private static final String CODIGO_BOLETO = RandomStringUtils.randomNumeric(45);
	private static final LocalDate DATA_PADRAO = LocalDate.of(2022, Month.DECEMBER, 27);
	private static final String DOCUMENTO_DESTINATARIO = RandomStringUtils.randomNumeric(15);
	private static final String NOME_DESTINATARIO = "Microhouse Informática S/C Ltda";
	private static final String TIPO_BOLETO = "NPC";
	private static final BigDecimal VALOR_BOLETO = BigDecimal.valueOf(260.0);

	static private RecuperadorBoletos recuperador;

	static private Autenticador autenticador;
	static private BuildersApiProvider provider;

	@BeforeAll
	static void instanciaRecuperadorInjetandoMocks() {
		RecuperadorBoletosTest.autenticador = Mockito.mock(Autenticador.class);
		RecuperadorBoletosTest.provider = Mockito.mock(BuildersApiProvider.class);
		RecuperadorBoletosTest.recuperador = new RecuperadorBoletos(autenticador, provider);
	}

	@Test
	void deveEstourarErroDeComunicacao_casoRequisicaoFalhe_quandoRecuperandoPeloCodigoDeBoleto()
			throws UnirestException {
		mockErroAplicacaoTerceira();
		assertThrows(ComunicacaoApiBuildersException.class, () -> recuperador.recupera(null));
	}

	@Test
	void deveEstourarErroDeCodigoBoletoInvalido_casoRespostaDaRequisicaoNaoSejaOK_quandoRecuperandoPeloCodigoDeBoleto()
			throws UnirestException {
		mockRespostaAplicacaoTerceira(HttpStatus.BAD_REQUEST, null);
		assertThrows(CodigoBoletoInvalidoException.class, () -> recuperador.recupera(null));
	}

	@Test
	void deveRetornarBoleto_casoRespostaDaRequisicaoSejaOK_quandoRecuperandoPeloCodigoDeBoleto()
			throws UnirestException, CodigoBoletoInvalidoException {
		mockRespostaAplicacaoTerceira(HttpStatus.OK, mockBoleto());
		Boleto boletoRecuperado = recuperador.recupera(CODIGO_BOLETO);

		Assertions.assertEquals(CODIGO_BOLETO, boletoRecuperado.getCodigo());
		Assertions.assertEquals(DATA_PADRAO, boletoRecuperado.getDataVencimento());
		Assertions.assertEquals(DOCUMENTO_DESTINATARIO, boletoRecuperado.getDocumentoDestinatario());
		Assertions.assertEquals(NOME_DESTINATARIO, boletoRecuperado.getNomeDestinatario());
		Assertions.assertEquals(TIPO_BOLETO, boletoRecuperado.getTipo());
		Assertions.assertEquals(VALOR_BOLETO, boletoRecuperado.getValor());
	}

	private Boleto mockBoleto() {
		Boleto boleto = new Boleto();
		boleto.setCodigo(CODIGO_BOLETO);
		boleto.setDataVencimento(DATA_PADRAO);
		boleto.setDocumentoDestinatario(DOCUMENTO_DESTINATARIO);
		boleto.setNomeDestinatario(NOME_DESTINATARIO);
		boleto.setTipo(TIPO_BOLETO);
		boleto.setValor(VALOR_BOLETO);
		return boleto;
	}

	private void mockErroAplicacaoTerceira() throws UnirestException {
		RequestBodyEntity bodyEntity = Mockito.mock(RequestBodyEntity.class);
		Mockito.doThrow(UnirestException.class).when(bodyEntity).asObject(Boleto.class);

		HttpRequestWithBody requestWithBody = Mockito.mock(HttpRequestWithBody.class);
		Mockito.doReturn(requestWithBody).when(requestWithBody).headers(anyMap());
		Mockito.doReturn(bodyEntity).when(requestWithBody).body(any(JsonNode.class));

		Mockito.doReturn(requestWithBody).when(provider).post(anyString());
	}

	@SuppressWarnings("unchecked")
	private void mockRespostaAplicacaoTerceira(HttpStatusCode statusCode, Boleto boleto) throws UnirestException {
		HttpResponse<Boleto> reponse = Mockito.mock(HttpResponse.class);
		Mockito.doReturn(statusCode.value()).when(reponse).getStatus();
		Mockito.doReturn(boleto).when(reponse).getBody();

		RequestBodyEntity bodyEntity = Mockito.mock(RequestBodyEntity.class);
		Mockito.doReturn(reponse).when(bodyEntity).asObject(Boleto.class);

		HttpRequestWithBody requestWithBody = Mockito.mock(HttpRequestWithBody.class);
		Mockito.doReturn(requestWithBody).when(requestWithBody).headers(anyMap());
		Mockito.doReturn(bodyEntity).when(requestWithBody).body(any(JsonNode.class));

		Mockito.doReturn(requestWithBody).when(provider).post(anyString());
	}

}
