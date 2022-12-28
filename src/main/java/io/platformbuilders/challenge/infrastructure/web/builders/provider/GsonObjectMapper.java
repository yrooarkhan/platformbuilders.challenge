package io.platformbuilders.challenge.infrastructure.web.builders.provider;

import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.ObjectMapper;

import io.platformbuilders.challenge.infrastructure.adapters.AdaptadorDeLocalDate;
import io.platformbuilders.challenge.infrastructure.adapters.EstrategiaDeSerializacaoPorAnnotation;

public class GsonObjectMapper implements ObjectMapper {

	private Gson gson;

	public GsonObjectMapper() {
		EstrategiaDeSerializacaoPorAnnotation serializador = new EstrategiaDeSerializacaoPorAnnotation();
		AdaptadorDeLocalDate adaptadorLocalDate = new AdaptadorDeLocalDate();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setFieldNamingStrategy(serializador).registerTypeAdapter(LocalDate.class, adaptadorLocalDate);
		this.gson = gsonBuilder.create();
	}

	@Override
	public <T> T readValue(String value, Class<T> valueType) {
		return gson.fromJson(value, valueType);
	}

	@Override
	public String writeValue(Object value) {
		return gson.toJson(value);
	}

	public Gson getGson() {
		return gson;
	}

}
