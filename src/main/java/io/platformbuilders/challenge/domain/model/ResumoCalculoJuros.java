package io.platformbuilders.challenge.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResumoCalculoJuros {

	@JsonProperty("original_amount")
	private BigDecimal valorOriginal;

	@JsonProperty("amount")
	private BigDecimal valorComJuros;

	@JsonProperty("due_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;

	@JsonProperty("payment_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;

	@JsonProperty("interest_amount_calculated")
	private BigDecimal quantidadeJurosCalculada;

	@JsonProperty("fine_amount_calculated")
	private BigDecimal quantidadeMultaCalculada;

	public ResumoCalculoJuros(BigDecimal valorOriginal, LocalDate dataVencimento, LocalDate dataPagamento) {
		this.valorOriginal = valorOriginal;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(BigDecimal valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public BigDecimal getValorComJuros() {
		return valorComJuros;
	}

	public void setValorComJuros(BigDecimal valor) {
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

	public BigDecimal getQuantidadeJurosCalculada() {
		return quantidadeJurosCalculada;
	}

	public void setQuantidadeJurosCalculada(BigDecimal quantidadeJurosCalculada) {
		this.quantidadeJurosCalculada = quantidadeJurosCalculada;
	}

	public BigDecimal getQuantidadeMultaCalculada() {
		return quantidadeMultaCalculada;
	}

	public void setQuantidadeMultaCalculada(BigDecimal quantidadeMultaCalculada) {
		this.quantidadeMultaCalculada = quantidadeMultaCalculada;
	}

}
