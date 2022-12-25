package io.platformbuilders.challenge.infrastructure.web.builders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.platformbuilders.challenge.infrastructure.web.builders.ConfiguracaoApiBuilders;

class ConfiguracaoApiBuildersTest {

	private static final String MENSAGEM_EXCECAO = "Não foi possível instanciar a classe ConfiguracaoApiBuilders, pois as propriedades \"clientId\" ou \"clientSecret\" não foram definidas nas propriedades do sistema.";
	private static final String CLIENT_ID = "clientId";
	private static final String CLIENT_SECRET = "clientSecret";
	private static final String STRING_VAZIA = "";

	private static final String MOCK_CLIENT_ID = "d33113a8-249d-4074-b538-75fdc10d1cfd";
	private static final String MOCK_CLIENT_SECRET = "cbe9f7ac-a7cb-4877-8118-bde08f72110d";

	@BeforeEach
	void limpaPropriedadesDoSistema() {
		System.clearProperty(CLIENT_ID);
		System.clearProperty(CLIENT_SECRET);
	}

	@Test
	void deveEstourarExcecao_casoClientSecretForNulo_quandoInstanciandoClasseDeConfiguracao() {
		System.setProperty(CLIENT_ID, MOCK_CLIENT_ID);
		IllegalArgumentException excecao = assertThrowsAoInstanciarConfiguracao();
		Assertions.assertEquals(MENSAGEM_EXCECAO, excecao.getMessage());
	}

	@Test
	void deveEstourarExcecao_casoClientIdForNulo_quandoInstanciandoClasseDeConfiguracao() {
		System.setProperty(CLIENT_SECRET, MOCK_CLIENT_SECRET);
		IllegalArgumentException excecao = assertThrowsAoInstanciarConfiguracao();
		Assertions.assertEquals(MENSAGEM_EXCECAO, excecao.getMessage());
	}

	@Test
	void deveEstourarExcecao_casoClientIdForVazio_quandoInstanciandoClasseDeConfiguracao() {
		System.setProperty(CLIENT_ID, STRING_VAZIA);
		System.setProperty(CLIENT_SECRET, MOCK_CLIENT_SECRET);

		IllegalArgumentException excecao = assertThrowsAoInstanciarConfiguracao();

		Assertions.assertEquals(MENSAGEM_EXCECAO, excecao.getMessage());
	}

	@Test
	void deveEstourarExcecao_casoClientSecretForVazio_quandoInstanciandoClasseDeConfiguracao() {
		System.setProperty(CLIENT_ID, MOCK_CLIENT_ID);
		System.setProperty(CLIENT_SECRET, STRING_VAZIA);

		IllegalArgumentException excecao = assertThrowsAoInstanciarConfiguracao();

		Assertions.assertEquals(MENSAGEM_EXCECAO, excecao.getMessage());
	}

	@Test
	void deveRetornarValoresConfigurados_casoClientIdEClientSecretForemPopulados_quandoChamandoOsGettersDaConfiguracao() {
		System.setProperty(CLIENT_ID, MOCK_CLIENT_ID);
		System.setProperty(CLIENT_SECRET, MOCK_CLIENT_SECRET);

		ConfiguracaoApiBuilders configuracao = new ConfiguracaoApiBuilders();

		Assertions.assertEquals(MOCK_CLIENT_ID, configuracao.clientId());
		Assertions.assertEquals(MOCK_CLIENT_SECRET, configuracao.clientSecret());
	}

	private IllegalArgumentException assertThrowsAoInstanciarConfiguracao() {
		return Assertions.assertThrows(IllegalArgumentException.class, ConfiguracaoApiBuilders::new);
	}

}
