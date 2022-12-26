package io.platformbuilders.challenge.infrastructure.web.builders.provider;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;

import io.platformbuilders.challenge.infrastructure.EstrategiaDeSerializacaoPorAnnotation;

@Component
public class AutenticadorApiBuildersProvider {

	private ConfiguracaoApiBuilders configuracao;
	private Gson gson;

	@Autowired
	public AutenticadorApiBuildersProvider(ConfiguracaoApiBuilders configuracao) {
		this.gson = new GsonBuilder().setFieldNamingStrategy(new EstrategiaDeSerializacaoPorAnnotation()).create();
		this.configuracao = configuracao;
	}

	public String getConfiguracao() {
		return gson.toJson(this.configuracao);
	}

	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	public HttpRequestWithBody post(String url) {
		return Unirest.post(url);
	}

}
