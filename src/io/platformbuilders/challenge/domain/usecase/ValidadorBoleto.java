package io.platformbuilders.challenge.domain.usecase;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.usecase.exception.BoletoNaoVencidoException;
import io.platformbuilders.challenge.domain.usecase.exception.TipoBoletoDiferenteNpcException;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

@Component
public class ValidadorBoleto {

	private static final String TIPO_BOLETO_NPC = "NPC";

	public void valida(Boleto boleto, PagamentoBoleto pagamentoBoleto) throws CalculadoraException {
		validaDataPagamento(pagamentoBoleto, boleto);
		validaTipoBoleto(boleto);
	}

	private void validaDataPagamento(PagamentoBoleto pagamentoBoleto, Boleto boleto) throws BoletoNaoVencidoException {
		LocalDate dataPagamentoBoleto = pagamentoBoleto.getDataPagamento();
		LocalDate dataVencimento = boleto.getDataVencimento();
		if (dataPagamentoBoleto.isBefore(dataVencimento) || dataPagamentoBoleto.isEqual(dataVencimento)) {
			throw new BoletoNaoVencidoException();
		}
	}

	private void validaTipoBoleto(Boleto boleto) throws TipoBoletoDiferenteNpcException {
		if (!TIPO_BOLETO_NPC.equals(boleto.getTipo())) {
			throw new TipoBoletoDiferenteNpcException();
		}
	}

}
