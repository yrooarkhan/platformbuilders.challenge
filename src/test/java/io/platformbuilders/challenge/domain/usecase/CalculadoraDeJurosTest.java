package io.platformbuilders.challenge.domain.usecase;

import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.domain.usecase.exception.CodigoBoletoInvalidoException;
import io.platformbuilders.challenge.domain.usecase.exception.InformacoesInsuficientesException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoDeCalculosRepository;
import io.platformbuilders.challenge.infrastructure.web.builders.RecuperadorBoletos;

class CalculadoraDeJurosTest {

	private static final BigDecimal VALOR_COM_JUROS = BigDecimal.valueOf(268.09);
	private static final BigDecimal MULTA_CALCULADA = BigDecimal.valueOf(5.21);
	private static final BigDecimal JUROS_CALCULADO = BigDecimal.valueOf(2.58);
	private static final LocalDate DATA_PAGAMENTO_30_DIAS_DEPOIS = LocalDate.of(2022, Month.DECEMBER, 31);
	private static final BigDecimal VALOR_BOLETO = BigDecimal.valueOf(260.3);
	private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2022, Month.DECEMBER, 1);
	private static final String TIPO_BOLETO = "NPC";
	private static final String DOCUMENTO_DESTINATARIO = RandomStringUtils.randomNumeric(15);
	private static final String NOME_DESTINATARIO = "Microhouse Informática S/C Ltda";
	private static final String STRING_VAZIA = "";
	private static final String CODIGO_BOLETO = RandomStringUtils.randomNumeric(45);

	private CalculadoraDeJuros calculadora;
	
	private RecuperadorBoletos recuperador;
	private ValidadorBoleto validador;
	private HistoricoDeCalculosRepository historicoRepository;

	@BeforeEach
	void instanciaCalculadoraInjetandoDependencias() throws CodigoBoletoInvalidoException {
		this.recuperador = Mockito.mock(RecuperadorBoletos.class);
		this.validador = Mockito.mock(ValidadorBoleto.class);
		this.historicoRepository = Mockito.mock(HistoricoDeCalculosRepository.class);

		mockRetornoBoletoDoRecuperador();

		this.calculadora = new CalculadoraDeJuros(recuperador, validador, historicoRepository);
	}

	@Test
	void deveEstourarErroInformacoesInsuficientes_casoPagamentoBoletoCodigoBarrasNulo_quandoCalculandoJuros() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(null);
		Assertions.assertThrows(InformacoesInsuficientesException.class,
				() -> calculadora.calculaJurosBoleto(pagamentoBoleto));
	}

	@Test
	void deveEstourarErroInformacoesInsuficientes_casoPagamentoBoletoCodigoBarrasVazio_quandoCalculandoJuros() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(STRING_VAZIA);
		Assertions.assertThrows(InformacoesInsuficientesException.class,
				() -> calculadora.calculaJurosBoleto(pagamentoBoleto));
	}

	@Test
	void deveEstourarErroInformacoesInsuficientes_casoPagamentoBoletoDataPagamentoNulo_quandoCalculandoJuros() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		pagamentoBoleto.setDataPagamento(null);
		Assertions.assertThrows(InformacoesInsuficientesException.class,
				() -> calculadora.calculaJurosBoleto(pagamentoBoleto));
	}

	@Test
	void deveRetonar_casoPagamentoBoletoDataPagamentoNulo_quandoCalculandoJuros() throws CalculadoraException {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto();
		ResumoCalculoJuros resumoCalculo = calculadora.calculaJurosBoleto(pagamentoBoleto);
		
		Assertions.assertEquals(DATA_PAGAMENTO_30_DIAS_DEPOIS, resumoCalculo.getDataPagamento());
		Assertions.assertEquals(DATA_VENCIMENTO, resumoCalculo.getDataVencimento());
		Assertions.assertEquals(JUROS_CALCULADO, resumoCalculo.getQuantidadeJurosCalculada());
		Assertions.assertEquals(MULTA_CALCULADA, resumoCalculo.getQuantidadeMultaCalculada());
		Assertions.assertEquals(VALOR_COM_JUROS, resumoCalculo.getValorComJuros());
		Assertions.assertEquals(VALOR_BOLETO, resumoCalculo.getValorOriginal());
	}

	private Boleto mockRetornoBoletoDoRecuperador() throws CodigoBoletoInvalidoException {
		Boleto boleto = new Boleto();
		boleto.setCodigo(CODIGO_BOLETO);
		boleto.setDataVencimento(DATA_VENCIMENTO);
		boleto.setDocumentoDestinatario(DOCUMENTO_DESTINATARIO);
		boleto.setNomeDestinatario(NOME_DESTINATARIO);
		boleto.setTipo(TIPO_BOLETO);
		boleto.setValor(VALOR_BOLETO);

		Mockito.doReturn(boleto).when(recuperador).recupera(anyString());
		return boleto;
	}

	private PagamentoBoleto mockPagamentoBoleto() {
		PagamentoBoleto pagamentoBoleto = new PagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(CODIGO_BOLETO);
		pagamentoBoleto.setDataPagamento(DATA_PAGAMENTO_30_DIAS_DEPOIS);
		return pagamentoBoleto;
	}

}
