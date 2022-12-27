package io.platformbuilders.challenge.infrastructure.persistance;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import io.platformbuilders.challenge.domain.model.ResumoCalculoJuros;

@Entity
@Table(name = "interest_calculation_history")
public class HistoricoCalculoJuros {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double valorOriginal;
	private Double valorComJuros;
	private Double percentualJuros;
	private Double percentualMulta;

	private LocalDate dataVencimento;
	private LocalDate dataPagamento;

	@CreationTimestamp
	private LocalDateTime dataDoCalculo;

	public HistoricoCalculoJuros(ResumoCalculoJuros resumoCalculo) {
		this.valorOriginal = resumoCalculo.getValorOriginal();
		this.valorComJuros = resumoCalculo.getValorComJuros();
		this.percentualJuros = resumoCalculo.getQuantidadeJurosCalculada();
		this.percentualMulta = resumoCalculo.getQuantidadeMultaCalculada();
		this.dataVencimento = resumoCalculo.getDataVencimento();
		this.dataPagamento = resumoCalculo.getDataPagamento();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setValorComJuros(Double valorComJuros) {
		this.valorComJuros = valorComJuros;
	}

	public Double getPercentualJuros() {
		return percentualJuros;
	}

	public void setPercentualJuros(Double percentualJuros) {
		this.percentualJuros = percentualJuros;
	}

	public Double getPercentualMulta() {
		return percentualMulta;
	}

	public void setPercentualMulta(Double percentualMulta) {
		this.percentualMulta = percentualMulta;
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

	public LocalDateTime getDataDoCalculo() {
		return dataDoCalculo;
	}

	public void setDataDoCalculo(LocalDateTime dataDoCalculo) {
		this.dataDoCalculo = dataDoCalculo;
	}

}
