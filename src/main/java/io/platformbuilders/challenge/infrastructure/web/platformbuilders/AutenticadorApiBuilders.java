package io.platformbuilders.challenge.infrastructure.web.platformbuilders;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.time.LocalDateTime;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.platformbuilders.challenge.infrastructure.exception.AutenticacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.exception.ComunicacaoApiBuildersException;
import io.platformbuilders.challenge.infrastructure.web.platformbuilders.provider.AutenticadorApiBuildersProvider;

public class AutenticadorApiBuilders {

	private static final String AUTH_URL = "https://vagas.builders/api/builders/auth/tokens";
	private static final String CHAVE_DE_AUTENTICAO = "token";
	private static final String EXPIRA_EM = "expires_in";

	private AutenticadorApiBuildersProvider provider;

	private String chaveAutenticacao;
	private LocalDateTime expiraEm;

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
			HttpResponse<JsonNode> resposta = provider.post(AUTH_URL).body(provider.getConfiguracao()).asJson();

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
