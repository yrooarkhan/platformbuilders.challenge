package io.platformbuilders.challenge.domain.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumoCalculoJuros {

	@JsonProperty("original_amount")
	private Double valorOriginal;

	@JsonProperty("amount")
	private Double valorComJuros;

	@JsonProperty("due_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;

	@JsonProperty("payment_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;

	@JsonProperty("interest_amount_calculated")
	private Double quantidadeJurosCalculada;

	@JsonProperty("fine_amount_calculated")
	private Double quantidadeMultaCalculada;

	public ResumoCalculoJuros(Double valorOriginal, LocalDate dataVencimento, LocalDate dataPagamento) {
		this.valorOriginal = valorOriginal;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
	}

	public Double getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(Double valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public Double getValorComJuros() {
		return valorComJuros;
	}

	public void setValorComJuros(Double valor) {
		this.valorComJuros = valor;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getQuantidadeJurosCalculada() {
		return quantidadeJurosCalculada;
	}

	public void setQuantidadeJurosCalculada(Double quantidadeJurosCalculada) {
		this.quantidadeJurosCalculada = quantidadeJurosCalculada;
	}

	public Double getQuantidadeMultaCalculada() {
		return quantidadeMultaCalculada;
	}

	public void setQuantidadeMultaCalculada(Double quantidadeMultaCalculada) {
		this.quantidadeMultaCalculada = quantidadeMultaCalculada;
	}

}