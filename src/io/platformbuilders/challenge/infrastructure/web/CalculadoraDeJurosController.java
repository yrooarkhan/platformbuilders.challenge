package io.platformbuilders.challenge.infrastructure.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.platformbuilders.challenge.domain.model.PagamentoBoleto;
import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;
import io.platformbuilders.challenge.domain.usecase.CalculadoraDeJuros;
import io.platformbuilders.challenge.infrastructure.exception.template.CalculadoraException;

@RestController
@RequestMapping("/api/v1/calculate-interests")
public class CalculadoraDeJurosController {

	@Autowired
	private CalculadoraDeJuros usecase;

	@PostMapping
	@ResponseBody
	public ResponseEntity<ResumoCalculoJuros> calculaJurosBoleto(@RequestBody PagamentoBoleto pagamentoBoleto)
			throws CalculadoraException {
		ResumoCalculoJuros jurosBoleto = usecase.calculaJurosBoleto(pagamentoBoleto);
		return new ResponseEntity<>(jurosBoleto, HttpStatus.CREATED);
	}

}
