package io.platformbuilders.challenge.domain.model;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FalhaProcessamento {

	@JsonProperty("error_code")
	private Integer codigoErro;

	@JsonProperty("description")
	private String descricao;

	public FalhaProcessamento(HttpStatusCode statusHttp, String descricao) {
		this.codigoErro = statusHttp.value();
		this.descricao = descricao;
	}

	public Integer getCodigoErro() {
		return codigoErro;
	}

	public String getDescricao() {
		return descricao;
	}

}
