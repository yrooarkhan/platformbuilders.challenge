package io.platformbuilders.challenge.infrastructure.web.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.platformbuilders.challenge.domain.model.CalculoJuros;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.usecase.BuildersPayUsecase;
import io.platformbuilders.challenge.domain.usecase.exception.BoletoNaoVencidoException;
import io.platformbuilders.challenge.domain.usecase.exception.TipoBoletoDiferenteNpcException;
import io.platformbuilders.challenge.infrastructure.LocalDateAdapter;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WebAppContext.class, BuildersPayExceptionHandler.class })
class BuildersPayControllerTest {

	private static final Double MULTA_CALCULADA = 2d;
	private static final Double JUROS_CALCULADO = 0.066;
	private static final Double VALOR_ORIGINAL = 1200d;
	private static final Double VALOR_JUROS = 1224.792;

	private static final String ERROR = "$.error_code";
	private static final String DESCRIPTION = "$.description";
	private static final String ERRO_BOLETO_NAO_NPC = "O código de boleto informado não pertence a um boleto do tipo NPC.";
	private static final String ERRO_BOLETO_NAO_VENCIDO = "O código de boleto informado não pertence a um boleto vencido.";
	private static final String URI = "/api/v1/calculate-interests";

	private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2022, Month.DECEMBER, 23);
	private static final LocalDate DATA_PAGAMENTO = LocalDate.of(2022, Month.DECEMBER, 25);

	private static final Integer TAMANHO_CODIGO_DE_BARRAS = 44;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private BuildersPayUsecase usecase;

	private MockMvc mockMvc;
	private Gson gson;

	@BeforeEach
	void configuraMockMvcEGsonParaTestes() {
		this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void deveEstourarErro_casoBoletoEnviadoNaoForDoTipoNPC_quandoCalculandoJurosAtravesDaAPI() throws Exception {
		doThrow(new TipoBoletoDiferenteNpcException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnprocessableEntity());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNPROCESSABLE_ENTITY.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(ERRO_BOLETO_NAO_NPC)));
	}

	@Test
	void deveEstourarErro_casoBoletoNaoEstiverVencido_quandoCalculandoJurosAtravesDaAPI() throws Exception {
		doThrow(new BoletoNaoVencidoException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnprocessableEntity());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNPROCESSABLE_ENTITY.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(ERRO_BOLETO_NAO_VENCIDO)));
	}

	@Test
	void deveEstourarErro_casoCodigoDoBoletoForInvalido_quandoCalculandoJurosAtravesDaAPI() throws Exception {
		doThrow(new BoletoNaoVencidoException()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isUnprocessableEntity());
		entidadeNaoProcessavel.andExpect(jsonPath(ERROR, Matchers.equalTo(UNPROCESSABLE_ENTITY.value())));
		entidadeNaoProcessavel.andExpect(jsonPath(DESCRIPTION, Matchers.equalTo(ERRO_BOLETO_NAO_VENCIDO)));
	}

	@Test
	void deveRetornarJurosCalculados_casoBoletoForValido_quandoCalculandoJurosAtravesDaAPI() throws Exception {
		Mockito.doReturn(mockCalculoJuros()).when(usecase).calculaJurosBoleto(any(PagamentoBoleto.class));

		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		String pagamentoBoletoJson = gson.toJson(pagamentoBoleto);
		ResultActions resultado = mockMvc.perform(post(URI).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		ResultActions entidadeNaoProcessavel = resultado.andExpect(status().isCreated());
		entidadeNaoProcessavel.andExpect(jsonPath("$.original_amount", Matchers.equalTo(VALOR_ORIGINAL)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.amount", Matchers.equalTo(VALOR_JUROS)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.due_date", Matchers.equalTo(DATA_VENCIMENTO.toString())));
		entidadeNaoProcessavel.andExpect(jsonPath("$.payment_date", Matchers.equalTo(DATA_PAGAMENTO.toString())));
		entidadeNaoProcessavel.andExpect(jsonPath("$.interest_amount_calculated", Matchers.equalTo(JUROS_CALCULADO)));
		entidadeNaoProcessavel.andExpect(jsonPath("$.fine_amount_calculated", Matchers.equalTo(MULTA_CALCULADA)));
	}

	private CalculoJuros mockCalculoJuros() {
		CalculoJuros calculoJuros = new CalculoJuros();
		calculoJuros.setValorOriginal(VALOR_ORIGINAL);
		calculoJuros.setValor(VALOR_JUROS);
		calculoJuros.setDataVencimento(DATA_VENCIMENTO);
		calculoJuros.setDataPagamento(DATA_PAGAMENTO);
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
