package io.platformbuilders.challenge.infrastructure.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.platformbuilders.challenge.domain.usecase.BuildersPayUsecase;

@Configuration
@ComponentScan(basePackages = "io.platformbuilders.challenge.domain.usecase")
public class WebAppContext implements WebMvcConfigurer {

	@Bean
	public BuildersPayUsecase usecase() {
		return new BuildersPayUsecase();
	}

}