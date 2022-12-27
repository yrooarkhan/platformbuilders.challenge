package io.platformbuilders.challenge.infrastructure.adapters;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class AdaptadorDeLocalDate extends TypeAdapter<LocalDate> {

	@Override
	public void write(JsonWriter saida, LocalDate data) throws IOException {
		saida.value(data.format(ofPattern("yyyy-MM-dd")));
	}

	@Override
	public LocalDate read(JsonReader entrada) throws IOException {
		return LocalDate.parse(entrada.nextString(), ofPattern("yyyy-MM-dd"));
	}

}