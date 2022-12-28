package io.platformbuilders.challenge.infrastructure.web.builders.provider;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;

@Component
public class BuildersApiProvider {

	private Configuracao configuracao;
	private Gson gson;

	@Autowired
	public BuildersApiProvider(Configuracao configuracao) {
		GsonObjectMapper gsonMapper = new GsonObjectMapper();
		Unirest.setObjectMapper(gsonMapper);
		this.gson = gsonMapper.getGson();
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
