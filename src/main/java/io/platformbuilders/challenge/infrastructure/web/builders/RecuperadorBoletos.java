package io.platformbuilders.challenge.infrastructure.web.builders;

import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import io.platformbuilders.challenge.domain.model.Boleto;
import io.platformbuilders.challenge.domain.usecase.exception.CodigoBoletoInvalidoException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.BuildersApiProvider;

@Component
public class RecuperadorBoletos {

	private static final String AUTHORIZATION = "Authorization";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String BUILDERS_URL = "https://vagas.builders/api/builders/bill-payments/codes";

	private Autenticador autenticador;
	private BuildersApiProvider provider;

	@Autowired
	public RecuperadorBoletos(Autenticador autenticador, BuildersApiProvider provider) {
		this.autenticador = autenticador;
		this.provider = provider;
	}

	public Boleto recupera(String codigoBoleto) throws CodigoBoletoInvalidoException {
		try {
			JsonNode corpoEnvio = new JsonNode(formataParaEnvio(codigoBoleto));
			HttpResponse<Boleto> resposta = montaRequisicaoComCorpo().body(corpoEnvio).asObject(Boleto.class);

			if (OK.value() != resposta.getStatus()) {
				throw new CodigoBoletoInvalidoException();
			}

			return resposta.getBody();
		} catch (UnirestException e) {
			throw new ComunicacaoApiBuildersException();
		}
	}

	private HttpRequestWithBody montaRequisicaoComCorpo() {
		Map<String, String> headers = new HashMap<>();
		headers.put(CONTENT_TYPE, APPLICATION_JSON);
		headers.put(AUTHORIZATION, autenticador.autentica());
		return provider.post(BUILDERS_URL).headers(headers);
	}

	private String formataParaEnvio(String codigoBoleto) {
		return "{ 'code': '" + codigoBoleto + "' }";
	}

}
