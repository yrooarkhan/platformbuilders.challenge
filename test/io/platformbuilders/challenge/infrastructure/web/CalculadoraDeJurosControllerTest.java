package io.platformbuilders.challenge.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.time.LocalDate;
import java.time.Month;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.domain.usecase.CalculadoraDeJuros;
import io.platformbuilders.challenge.domain.usecase.exception.BoletoNaoVencidoException;
import io.platformbuilders.challenge.domain.usecase.exception.CodigoBoletoInvalidoException;
import io.platformbuilders.challenge.domain.usecase.exception.InformacoesInsuficientesException;
import io.platformbuilders.challenge.domain.usecase.exception.TipoBoletoDiferenteNpcException;
import io.platformbuilders.challenge.infrastructure.WebAppContextTest;
import io.platformbuilders.challenge.infrastructure.adapters.AdaptadorDeLocalDate;
import io.platformbuilders.challenge.infrastructure.exception.AutenticacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;

@WebAppConfiguration
@EnableJpaRepositories
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebAppContextTest.class })
class CalculadoraDeJurosControllerTest {

	private static final String COMUNICACAO_API_BUILDERS = "Não foi possível se comunicar com a aplicação terceira API Builders.";
	private static final String AUTENTICACAO_API_BUILDERS = "Não foi possível se autenticar com a aplicação terceira API Builders com as credenciais informadas.";
	private static final String BOLETO_DIFERENTE_NPC = "O código de boleto informado não pertence a um boleto do tipo NPC.";
	private static final String CAMPOS_OBRIGATORIOS = "O código de boleto (bar_code) e data de pagamento (payment_date) são obrigatórios para calcular o juros do boleto.";
	private static final String BOLETO_INEXISTENTE = "O código de boleto informado não pertence nenhum boleto da nossa base de dados.";
	private static final String BOLETO_NAO_VENCIDO = "O código de boleto informado não pertence a um boleto vencido.";
	private static final String FORMATO_INVALIDO = "Um ou mais campos informados estão em um formato inválido. Por favor, verifique a documentação e tente novamente.";
	private static final String ERRO_INESPERADO = "Um erro inesperado ocorreu. Por favor, tente novamente. Se o erro persistir, entre em contato com nosso suporte.";
	private static final String MENSAGEM_ERRO_GENERICA = "Exemplo de mensagem";

	private static final String ERROR = "$.error_code";
	private static final String DESCRIPTION = "$.description";
	private static final String URI = "/api/v1/calculate-interests";

	private static final Double MULTA_CALCULADA = 2d;
	private static final Double JUROS_CALCULADO = 0.066;
	private static final Double VALOR_ORIGINAL = 1200d;
	private static final Double VALOR_JUROS = 1224.792;

	private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2022, Month.DECEMBER, 23);
	private static final LocalDate DATA_PAGAMENTO = LocalDate.of(2022, Month.DECEMBER, 25);

	private static final Integer TAMANHO_CODIGO_DE_BARRAS = 44;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private CalculadoraDeJuros usecase;

	private MockMvc mockMvc;
	private Gson gson;

	@BeforeEach
	void configuraMockMvcEGsonParaTestes() {
		this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new AdaptadorDeLocalDate()).create();
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoBoletoNaoVencidoException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new BoletoNaoVencidoException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnprocessableEntity());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNPROCESSABLE_ENTITY.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(BOLETO_NAO_VENCIDO)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoCodigoBoletoInvalidoException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new CodigoBoletoInvalidoException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isBadRequest());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(BAD_REQUEST.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(BOLETO_INEXISTENTE)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoInformacoesInsuficientesException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new InformacoesInsuficientesException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isBadRequest());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(BAD_REQUEST.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(CAMPOS_OBRIGATORIOS)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoTipoBoletoDiferenteNpcException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new TipoBoletoDiferenteNpcException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnprocessableEntity());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNPROCESSABLE_ENTITY.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(BOLETO_DIFERENTE_NPC)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoAutenticacaoApiBuildersException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new AutenticacaoApiBuildersException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnauthorized());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNAUTHORIZED.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(AUTENTICACAO_API_BUILDERS)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDoTipoComunicacaoApiBuildersException_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(new ComunicacaoApiBuildersException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isServiceUnavailable());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(SERVICE_UNAVAILABLE.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(COMUNICACAO_API_BUILDERS)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDevidoADeserializacaoDoObjetoRecebido_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(HttpMessageNotReadableException.class).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isBadRequest());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(BAD_REQUEST.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(FORMATO_INVALIDO)));
	}

	@Test
	void deveRetornarMensagemDeErroTratada_casoErroSejaDevidoAUmProblemaInterno_quandoCalculandoJurosAtravesDaAPI()
			throws Exception {
		doThrow(RuntimeException.class).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isInternalServerError());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(INTERNAL_SERVER_ERROR.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(ERRO_INESPERADO)));
	}

	@Test
	void deveRetornarJurosCalculados_casoBoletoForValido_quandoCalculandoJurosAtravesDaAPI() throws Exception {
		Mockito.doReturn(mockCalculoJuros()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isOk());
		entidadeNaoProcessavel.andExpect(jsonPath("$.original_amount", Matchers.equalTo(VALOR_ORIGINAL)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.amount", Matchers.equalTo(VALOR_JUROS)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.due_date", Matchers.equalTo(DATA_VENCIMENTO.toString())));
		entidadeNaoProcessavel.andExpect(jsonPath("$.payment_date", Matchers.equalTo(DATA_PAGAMENTO.toString())));
		entidadeNaoProcessavel.andExpect(jsonPath("$.interest_amount_calculated", Matchers.equalTo(JUROS_CALCULADO)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.fine_amount_calculated", Matchers.equalTo(MULTA_CALCULADA)));
	}

	private ResumoCalculoJuros mockCalculoJuros() {
		ResumoCalculoJuros calculoJuros = new ResumoCalculoJuros(VALOR_ORIGINAL, DATA_VENCIMENTO, DATA_PAGAMENTO);
		calculoJuros.setValorComJuros(VALOR_JUROS);
		calculoJuros.setQuantidadeJurosCalculada(JUROS_CALCULADO);
		calculoJuros.setQuantidadeMultaCalculada(MULTA_CALCULADA);
		return calculoJuros;
	}

	private PagamentoBoleto mockPagamentoBoleto() {
		PagamentoBoleto pagamentoBoleto = new PagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(RandomStringUtils.randomAlphanumeric(TAMANHO_CODIGO_DE_BARRAS));
		pagamentoBoleto.setDataPagamento(DATA_PAGAMENTO);
		return pagamentoBoleto;
	}

}
