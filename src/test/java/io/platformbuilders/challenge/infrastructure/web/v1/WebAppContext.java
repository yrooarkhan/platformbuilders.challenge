package io.platformbuilders.challenge.infrastructure.web.v1;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.platformbuilders.challenge.domain.usecase.BuildersPayUsecase;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "io.platformbuilders.challenge")
public class WebAppContext implements WebMvcConfigurer {

	@Bean
	public BuildersPayController buildersPayController() {
		return new BuildersPayController();
	}

	@Bean
	public BuildersPayExceptionHandler buildersPayExceptionHandler() {
		return new BuildersPayExceptionHandler();
	}

	@Bean
	public BuildersPayUsecase buildersPayUsecase() {
		return Mockito.mock(BuildersPayUsecase.class);
	}

}
