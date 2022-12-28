package io.platformbuilders.challenge.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.Gson;

import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoCalculoJuros;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoDeCalculosRepository;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.GsonObjectMapper;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ChallengeApplication.class)
class ChallengeApplicationTest {

	private static final String CODIGO_BOLETO = "34191790010104351004791020150008291070026000";
	private static final String APPLICATION_JSON = "application/json";
	private static final String URL = "/api/v1/calculate-interests";
	
	private static final LocalDate DATA_PAGAMENTO = LocalDate.of(2022, Month.SEPTEMBER, 4);
	private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2022, Month.SEPTEMBER, 3);
	
	private static final Double VALOR_ORIGINAL = 260.0;
	private static final Double VALOR_JUROS = 265.29;
	private static final Double JUROS_CALCULADO = 0.09;
	private static final Double MULTA_CALCULADA = 5.20;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private HistoricoDeCalculosRepository historicoRepository;
	
	private Gson gson;
	
	@BeforeEach
	void configuraGsonParaTestes() {
		this.gson = new GsonObjectMapper().getGson();
	}

	@Test
	void testeDeIntegracao() throws Exception {
		String pagamentoBoletoJson = gson.toJson(geraPagamentoBoleto());
		ResultActions resultado = mvc.perform(post(URL).contentType(APPLICATION_JSON).content(pagamentoBoletoJson));

		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.original_amount", Matchers.equalTo(VALOR_ORIGINAL)));
		resultado.andExpect(jsonPath("$.amount", Matchers.equalTo(VALOR_JUROS)));
		resultado.andExpect(jsonPath("$.due_date", Matchers.equalTo(DATA_VENCIMENTO.toString())));
		resultado.andExpect(jsonPath("$.payment_date", Matchers.equalTo(DATA_PAGAMENTO.toString())));
		resultado.andExpect(jsonPath("$.interest_amount_calculated", Matchers.equalTo(JUROS_CALCULADO)));
		resultado.andExpect(jsonPath("$.fine_amount_calculated", Matchers.equalTo(MULTA_CALCULADA)));
		
		Optional<HistoricoCalculoJuros> entidade = historicoRepository.findById(1L);
		
		HistoricoCalculoJuros historicoCalculo = entidade.get();
		Assertions.assertEquals(JUROS_CALCULADO, historicoCalculo.getPercentualJuros());
		Assertions.assertEquals(MULTA_CALCULADA, historicoCalculo.getPercentualMulta());
		Assertions.assertEquals(VALOR_JUROS, historicoCalculo.getValorComJuros());
		Assertions.assertEquals(VALOR_ORIGINAL, historicoCalculo.getValorOriginal());
		Assertions.assertEquals(DATA_PAGAMENTO, historicoCalculo.getDataPagamento());
		Assertions.assertEquals(DATA_VENCIMENTO, historicoCalculo.getDataVencimento());
	}

	private PagamentoBoleto geraPagamentoBoleto() {
		PagamentoBoleto pagamentoBoleto = new PagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(CODIGO_BOLETO);
		pagamentoBoleto.setDataPagamento(DATA_PAGAMENTO);
		return pagamentoBoleto;
	}

	static {
		System.setProperty("clientId", "bd753592-cf9b-4d1a-96b9-cb8b2c01bd12");
		System.setProperty("clientSecret", "4e8229fe-1131-439c-9846-799895a8be5b");
	}

}
