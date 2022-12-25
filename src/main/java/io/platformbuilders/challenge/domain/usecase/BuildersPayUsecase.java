package io.platformbuilders.challenge.domain.usecase;

import org.springframework.stereotype.Service;

import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.infrastructure.exception.template.BoletoException;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;

@Service
public class BuildersPayUsecase {

	public ResumoCalculoJuros calculaJurosBoleto(PagamentoBoleto pagamentoBoleto) throws BoletoException {
		// TODO: implementação...
		return null;
	}

}
