package io.platformbuilders.challenge.infrastructure;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

	@Override
	public void write(JsonWriter out, LocalDate data) throws IOException {
		out.value(data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Override
	public LocalDate read(JsonReader in) throws IOException {
		return LocalDate.parse(in.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

}