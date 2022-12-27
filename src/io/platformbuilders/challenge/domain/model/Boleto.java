package io.platformbuilders.challenge.domain.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Boleto {

	@JsonProperty("code")
	private String codigo;

	@JsonProperty("due_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;

	@JsonProperty("amount")
	private Double valor;

	@JsonProperty("recipient_name")
	private String nomeDestinatario;

	@JsonProperty("recipient_document")
	private String documentoDestinatario;

	@JsonProperty("type")
	private String tipo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getDocumentoDestinatario() {
		return documentoDestinatario;
	}

	public void setDocumentoDestinatario(String documentoDestinatario) {
		this.documentoDestinatario = documentoDestinatario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
