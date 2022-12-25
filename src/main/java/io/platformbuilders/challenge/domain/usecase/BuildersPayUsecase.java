package io.platformbuilders.challenge.domain.usecase;

import org.springframework.stereotype.Service;

import io.platformbuilders.challenge.domain.model.CalculoJuros;
import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.infrastructure.exception.BuildersPayException;

@Service
public class BuildersPayUsecase {

	public CalculoJuros calculaJurosBoleto(PagamentoBoleto pagamentoBoleto) throws BuildersPayException {
		// TODO: implementação...
		return null;
	}

}
