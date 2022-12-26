package io.platformbuilders.challenge.domain.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.infrastructure.exception.template.BoletoException;
import io.platformbuilders.challenge.infrastructure.web.builders.AutenticadorApiBuilders;

@Service
public class BuildersPayUsecase {

	@Autowired
	private AutenticadorApiBuilders autenticador;

	public ResumoCalculoJuros calculaJurosBoleto(PagamentoBoleto pagamentoBoleto) throws BoletoException {
		System.out.println(autenticador.autentica());
		return null;
	}

}
