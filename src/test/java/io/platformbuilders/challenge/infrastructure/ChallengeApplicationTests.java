package io.platformbuilders.challenge.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import io.platformbuilders.challenge.domain.usecase.BuildersPayUsecase;
import io.platformbuilders.challenge.infrastructure.web.WebAppContext;

@SpringBootTest
@ContextConfiguration(classes = { WebAppContext.class })
class ChallengeApplicationTests {

	@Autowired
	public ApplicationContext contexto;

	@Test
	void testeInicializacao() {
		assertThat(contexto).isNotNull();
		assertThat(contexto.getBeansOfType(BuildersPayUsecase.class)).isNotEmpty();
	}

}
