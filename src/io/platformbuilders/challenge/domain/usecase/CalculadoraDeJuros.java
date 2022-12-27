package io.platformbuilders.challenge.domain.usecase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.domain.usecase.exception.InformacoesInsuficientesException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;
import io.platformbuilders.challenge.infrastructure.web.builders.RecuperadorBoletos;

@Service
public class CalculadoraDeJuros {

	private static final double MULTA_ATRASO = 0.02d;
	private static final double JUROS_AO_DIA = 0.0033d;

	private RecuperadorBoletos recuperador;
	private ValidadorBoleto validador;
//	private PersistidorDeHistoricoDeCalculo persistidorHistorico;

	@Autowired
	public CalculadoraDeJuros(RecuperadorBoletos recuperador, ValidadorBoleto validador) {
		this.recuperador = recuperador;
		this.validador = validador;
//		this.persistidorHistorico = persistidorHistorico;
	}

	public ResumoCalculoJuros calculaJurosBoleto(PagamentoBoleto pagamentoBoleto) throws CalculadoraException {
		validaInformacoesBasicas(pagamentoBoleto);

		Boleto boleto = recuperador.recupera(pagamentoBoleto.getCodigoBarras());
		validador.valida(boleto, pagamentoBoleto);

		return calcula(boleto, pagamentoBoleto);
	}

	private ResumoCalculoJuros calcula(Boleto boleto, PagamentoBoleto pagamentoBoleto) {
		Double valor = boleto.getValor();
		LocalDate dataVencimento = boleto.getDataVencimento();
		LocalDate dataPagamento = pagamentoBoleto.getDataPagamento();

		ResumoCalculoJuros resumo = new ResumoCalculoJuros(valor, dataVencimento, dataPagamento);
		long diasEmAtraso = dataVencimento.until(dataPagamento, ChronoUnit.DAYS);
		double percentualJurosAplicado = diasEmAtraso * JUROS_AO_DIA;
		double juros = boleto.getValor() * percentualJurosAplicado;
		double multa = boleto.getValor() * MULTA_ATRASO;

		resumo.setValorComJuros(boleto.getValor() + juros + multa);
		resumo.setQuantidadeJurosCalculada(juros);
		resumo.setQuantidadeMultaCalculada(multa);

		return resumo;
	}

	private void validaInformacoesBasicas(PagamentoBoleto pagamentoBoleto) throws InformacoesInsuficientesException {
		if (pagamentoBoleto.isInvalido()) {
			throw new InformacoesInsuficientesException();
		}
	}

}
