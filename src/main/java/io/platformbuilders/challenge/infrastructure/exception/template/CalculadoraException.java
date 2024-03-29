package io.platformbuilders.challenge.infrastructure.exception.template;

import org.springframework.http.HttpStatusCode;

public abstract class CalculadoraException extends Exception implements ErroConhecido {

	private static final long serialVersionUID = -8467687632367919878L;

	private final HttpStatusCode statusHttp;

	protected CalculadoraException(HttpStatusCode statusHttp, String descricaoErro) {
		super(descricaoErro);
		this.statusHttp = statusHttp;
	}

	@Override
	public HttpStatusCode getStatusHttp() {
		return statusHttp;
	}

}
