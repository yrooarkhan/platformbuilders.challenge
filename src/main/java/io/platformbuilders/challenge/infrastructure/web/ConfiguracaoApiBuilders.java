package io.platformbuilders.challenge.infrastructure.web;

public class ConfiguracaoApiBuilders {

	private static final String CLIENT_ID = "clientId";
	private static final String CLIENT_SECRET = "clientSecret";
	private static final String ERRO_IMPOSSIVEL_INSTANCIAR = "Não foi possível instanciar a classe ConfiguracaoApiBuilders, pois as propriedades \"clientId\" ou \"clientSecret\" não foram definidas nas propriedades do sistema.";
	
	private String clientId;
	private String clientSecret;

	public ConfiguracaoApiBuilders() {
		this.clientId = System.getProperty(CLIENT_ID);
		this.clientSecret = System.getProperty(CLIENT_SECRET);

		if (propriedadesNaoForamDefinidas()) {
			throw new IllegalArgumentException(ERRO_IMPOSSIVEL_INSTANCIAR);
		}

	}

	public String clientId() {
		return clientId;
	}

	public String clientSecret() {
		return clientSecret;
	}

	private boolean propriedadesNaoForamDefinidas() {
		return propriedadesSaoNulas(clientId, clientSecret) || propriedadesSaoVazias(clientId, clientSecret);
	}

	private boolean propriedadesSaoNulas(String clientId, String clientSecret) {
		return clientId == null || clientSecret == null;
	}

	private boolean propriedadesSaoVazias(String clientId, String clientSecret) {
		return clientId.isBlank() || clientSecret.isBlank();
	}

}
