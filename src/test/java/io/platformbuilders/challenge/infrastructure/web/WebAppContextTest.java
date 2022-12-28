package io.platformbuilders.challenge.infrastructure.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.platformbuilders.challenge.domain.usecase.CalculadoraDeJuros;
import io.platformbuilders.challenge.infrastructure.persistance.HistoricoCalculoJuros;
import io.platformbuilders.challenge.infrastructure.web.builders.Autenticador;
import io.platformbuilders.challenge.infrastructure.web.builders.RecuperadorBoletos;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.BuildersApiProvider;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.Configuracao;

@EnableWebMvc
@ComponentScan(basePackages = "io.platformbuilders.challenge")
public class WebAppContextTest implements WebMvcConfigurer {

	@Bean
	public CalculadoraDeJurosController calculadoraDeJurosController() {
		return new CalculadoraDeJurosController();
	}

	@Bean
	public CalculadoraDeJurosExceptionHandler calculadoraDeJurosExceptionHandler() {
		return new CalculadoraDeJurosExceptionHandler();
	}

	@Bean
	public CalculadoraDeJuros calculadoraDeJuros() {
		return Mockito.mock(CalculadoraDeJuros.class);
	}

	@Bean
	public Autenticador autenticador() {
		return Mockito.mock(Autenticador.class);
	}

	@Bean
	public Configuracao configuracao() {
		return Mockito.mock(Configuracao.class);
	}

	@Bean
	public BuildersApiProvider buildersApiProvider() {
		return Mockito.mock(BuildersApiProvider.class);
	}

	@Bean
	public RecuperadorBoletos recuperadorBoletos() {
		return Mockito.mock(RecuperadorBoletos.class);
	}

	@Bean
	public HistoricoCalculoJuros historicoCalculoJuros() {
		return Mockito.mock(HistoricoCalculoJuros.class);
	}

}
