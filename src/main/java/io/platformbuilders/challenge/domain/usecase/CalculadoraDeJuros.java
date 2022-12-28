package io.platformbuilders.challenge.domain.usecase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.domain.usecase.exception.InformacoesInsuficientesException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoCalculoJuros;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoDeCalculosRepository;
import io.platformbuilders.challenge.infrastructure.web.builders.RecuperadorBoletos;

@Service
public class CalculadoraDeJuros {

	private static final BigDecimal MULTA_ATRASO = BigDecimal.valueOf(0.02);
	private static final BigDecimal JUROS_AO_DIA = BigDecimal.valueOf(0.00033);

	private RecuperadorBoletos recuperador;
	private ValidadorBoleto validador;
	private HistoricoDeCalculosRepository historicoRepository;

	@Autowired
	public CalculadoraDeJuros(RecuperadorBoletos recuperador, ValidadorBoleto validador, HistoricoDeCalculosRepository persistidorHistorico) {
		this.recuperador = recuperador;
		this.validador = validador;
		this.historicoRepository = persistidorHistorico;
	}

	public ResumoCalculoJuros calculaJurosBoleto(PagamentoBoleto pagamentoBoleto) throws CalculadoraException {
		validaInformacoesBasicas(pagamentoBoleto);

		Boleto boleto = recuperador.recupera(pagamentoBoleto.getCodigoBarras());
		validador.valida(boleto, pagamentoBoleto);
		ResumoCalculoJuros resumoCalculo = calcula(boleto, pagamentoBoleto);
		historicoRepository.save(new HistoricoCalculoJuros(resumoCalculo));
		
		return resumoCalculo;
	}

	private ResumoCalculoJuros calcula(Boleto boleto, PagamentoBoleto pagamentoBoleto) {
		BigDecimal valorBoleto = boleto.getValor();
		LocalDate dataVencimento = boleto.getDataVencimento();
		LocalDate dataPagamento = pagamentoBoleto.getDataPagamento();
		ResumoCalculoJuros resumo = new ResumoCalculoJuros(valorBoleto, dataVencimento, dataPagamento);

		long diasEmAtraso = dataVencimento.until(dataPagamento, ChronoUnit.DAYS);
		BigDecimal jurosProporcional = BigDecimal.valueOf(diasEmAtraso).multiply(JUROS_AO_DIA);
		BigDecimal juros = valorBoleto.multiply(jurosProporcional).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal multa = valorBoleto.multiply(MULTA_ATRASO).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal valorComMultaEJuros = valorBoleto.add(juros).add(multa);
		
		resumo.setValorComJuros(valorComMultaEJuros.setScale(2, RoundingMode.DOWN));
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
