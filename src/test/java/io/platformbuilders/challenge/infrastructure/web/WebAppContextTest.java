package io.platformbuilders.challenge.infrastructure.web;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.platformbuilders.challenge.domain.usecase.BuildersPayUsecase;
import io.platformbuilders.challenge.infrastructure.web.builders.AutenticadorApiBuilders;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.AutenticadorApiBuildersProvider;
import io.platformbuilders.challenge.infrastructure.web.builders.provider.ConfiguracaoApiBuilders;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "io.platformbuilders.challenge")
public class WebAppContextTest implements WebMvcConfigurer {

	@Bean
	public BuildersPayController buildersPayController() {
		return new BuildersPayController();
	}

	@Bean
	public BuildersPayExceptionHandler buildersPayExceptionHandler() {
		return new BuildersPayExceptionHandler();
	}

	@Bean
	public BuildersPayUsecase usecase() {
		return Mockito.mock(BuildersPayUsecase.class);
	}

	@Bean
	public AutenticadorApiBuilders autenticadorApiBuilders() {
		return Mockito.mock(AutenticadorApiBuilders.class);
	}

	@Bean
	public ConfiguracaoApiBuilders configuracaoApiBuilders() {
		return Mockito.mock(ConfiguracaoApiBuilders.class);
	}

	@Bean
	public AutenticadorApiBuildersProvider autenticadorApiBuildersProvider() {
		return Mockito.mock(AutenticadorApiBuildersProvider.class);
	}

}
