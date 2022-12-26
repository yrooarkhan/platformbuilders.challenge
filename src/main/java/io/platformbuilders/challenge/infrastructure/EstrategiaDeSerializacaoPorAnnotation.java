package io.platformbuilders.challenge.infrastructure;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.FieldNamingStrategy;

public class EstrategiaDeSerializacaoPorAnnotation implements FieldNamingStrategy {

	@Override
	public String translateName(Field campo) {
		JsonProperty propriedade = campo.getAnnotation(JsonProperty.class);

		if (propriedade != null && !propriedade.value().isEmpty()) {
			return propriedade.value();
		}

		return campo.getName();
	}

}
