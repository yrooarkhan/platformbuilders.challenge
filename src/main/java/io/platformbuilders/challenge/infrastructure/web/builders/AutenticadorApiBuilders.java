package io.platformbuilders.challenge.infrastructure.web.builders;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import io.platformbuilders.challenge.infrastructure.exception.AutenticacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.AutenticadorApiBuildersProvider;

@Component
public class AutenticadorApiBuilders {

	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String AUTH_URL = "https://vagas.builders/api/builders/auth/tokens";
	private static final String CHAVE_DE_AUTENTICAO = "token";
	private static final String EXPIRA_EM = "expires_in";

	private String chaveAutenticacao;
	private LocalDateTime expiraEm;
	private AutenticadorApiBuildersProvider provider;

	@Autowired
	public AutenticadorApiBuilders(AutenticadorApiBuildersProvider provider) {
		this.provider = provider;
	}

	public String autentica() {
		if (chaveAutenticacao == null || chaveExpirou()) {
			JSONObject objetoResposta = autenticaComBaseNaConfiguracao().getBody().getObject();
			this.chaveAutenticacao = objetoResposta.getString(CHAVE_DE_AUTENTICAO);
			this.expiraEm = LocalDateTime.parse(objetoResposta.getString(EXPIRA_EM));
		}

		return this.chaveAutenticacao;
	}

	private HttpResponse<JsonNode> autenticaComBaseNaConfiguracao() {
		try {
			HttpRequestWithBody requisicaoComUmCorpo = provider.post(AUTH_URL).header(CONTENT_TYPE, APPLICATION_JSON);
			HttpResponse<JsonNode> resposta = requisicaoComUmCorpo.body(provider.getConfiguracao()).asJson();

			if (CREATED.value() != resposta.getStatus()) {
				boolean naoAutorizado = UNAUTHORIZED.value() == resposta.getStatus();
				throw naoAutorizado ? new AutenticacaoApiBuildersException() : new ComunicacaoApiBuildersException();
			}

			return resposta;
		} catch (UnirestException excecao) {
			throw new ComunicacaoApiBuildersException();
		}
	}

	private boolean chaveExpirou() {
		LocalDateTime agora = provider.now();
		return agora.isAfter(expiraEm);
	}

}
