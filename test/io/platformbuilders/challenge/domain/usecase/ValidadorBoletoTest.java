package io.platformbuilders.challenge.domain.usecase;

import java.time.LocalDate;
import java.time.Month;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.usecase.exception.BoletoNaoVencidoException;
import io.platformbuilders.challenge.domain.usecase.exception.TipoBoletoDiferenteNpcException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

class ValidadorBoletoTest {

	private static final String CODIGO_BOLETO = RandomStringUtils.randomNumeric(45);
	private static final String DOCUMENTO_DESTINATARIO = RandomStringUtils.randomNumeric(15);
	private static final String NOME_DESTINATARIO = "Microhouse Informática S/C Ltda";
	private static final Double VALOR_BOLETO = 260.0;
	private static final LocalDate DATA_VENCIMENTO = LocalDate.of(2022, Month.DECEMBER, 27);

	private static ValidadorBoleto validador;

	@BeforeAll
	static void instanciaValidador() {
		ValidadorBoletoTest.validador = new ValidadorBoleto();
	}
	
	@Test
	void deveEstourarExcecaoNaoBoletoVencido_casoPagamentoBoletoForAntesDoVencimento_quandoValidandoDataPagamento() throws CalculadoraException {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto(Pagamento.ADIANTADO);
		Boleto boleto = mockBoleto(TipoBoleto.NPC);
		Assertions.assertThrows(BoletoNaoVencidoException.class, () -> validador.valida(boleto, pagamentoBoleto));
	}

	@Test
	void deveEstourarExcecaoNaoBoletoVencido_casoPagamentoBoletoForNoDiaDoVencimento_quandoValidandoDataPagamento() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto(Pagamento.NO_DIA);
		Boleto boleto = mockBoleto(TipoBoleto.NPC);
		Assertions.assertThrows(BoletoNaoVencidoException.class, () -> validador.valida(boleto, pagamentoBoleto));
	}

	@Test
	void deveEstourarExcecaoTipoBoletoDiferenteNpc_casoTipoBoletoForDiferenteDeNpc_quandoValidandoTipoDeBoleto() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto(Pagamento.ATRASADO);
		Boleto boleto = mockBoleto(TipoBoleto.DIFERENTE_NPC);
		Assertions.assertThrows(TipoBoletoDiferenteNpcException.class, () -> validador.valida(boleto, pagamentoBoleto));
	}

	@Test
	void naoDeveEstourarExcecao_casoBoletoEstiverVencidoEForNpc_quandoValidando() {
		PagamentoBoleto pagamentoBoleto = mockPagamentoBoleto(Pagamento.ATRASADO);
		Boleto boleto = mockBoleto(TipoBoleto.NPC);
		Assertions.assertDoesNotThrow(() -> validador.valida(boleto, pagamentoBoleto));
	}

	private PagamentoBoleto mockPagamentoBoleto(Pagamento pagamento) {
		PagamentoBoleto pagamentoBoleto = new PagamentoBoleto();
		pagamentoBoleto.setCodigoBarras(CODIGO_BOLETO);
		pagamentoBoleto.setDataPagamento(pagamento.getDataPagamento());
		return pagamentoBoleto;
	}

	private Boleto mockBoleto(TipoBoleto tipoBoleto) {
		Boleto boleto = new Boleto();
		boleto.setCodigo(CODIGO_BOLETO);
		boleto.setDataVencimento(DATA_VENCIMENTO);
		boleto.setDocumentoDestinatario(DOCUMENTO_DESTINATARIO);
		boleto.setNomeDestinatario(NOME_DESTINATARIO);
		boleto.setTipo(tipoBoleto.toString());
		boleto.setValor(VALOR_BOLETO);
		return boleto;
	}

	private enum Pagamento {
		ATRASADO(LocalDate.of(2022, Month.DECEMBER, 31)),
		ADIANTADO(LocalDate.of(2022, Month.DECEMBER, 20)),
		NO_DIA(DATA_VENCIMENTO);

		private LocalDate dataPagamento;

		Pagamento(LocalDate dataPagamento) {
			this.dataPagamento = dataPagamento;
		}

		public LocalDate getDataPagamento() {
			return dataPagamento;
		}
	}

	private enum TipoBoleto { NPC, DIFERENTE_NPC }

}
