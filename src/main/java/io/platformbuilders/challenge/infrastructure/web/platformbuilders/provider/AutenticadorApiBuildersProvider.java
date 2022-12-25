package io.platformbuilders.challenge.infrastructure.web.platformbuilders.provider;

import java.time.LocalDateTime;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class AutenticadorApiBuildersProvider {

	private ConfiguracaoApiBuilders configuracao;

	public AutenticadorApiBuildersProvider(ConfiguracaoApiBuilders configuracao) {
		this.configuracao = configuracao;
	}

	public ConfiguracaoApiBuilders getConfiguracao() {
		return this.configuracao;
	}

	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	public HttpRequestWithBody post(String url) {
		return Unirest.post(url);
	}

}
