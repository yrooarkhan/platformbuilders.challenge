package io.platformbuilders.challenge.infrastructure.exception.template;

import org.springframework.http.HttpStatusCode;

public abstract class ApiBuildersException extends RuntimeException implements ErroConhecido {

	private static final long serialVersionUID = 2779802818923427914L;

	private final HttpStatusCode statusHttp;

	protected ApiBuildersException(HttpStatusCode statusHttp, String descricaoErro) {
		super(descricaoErro);
		this.statusHttp = statusHttp;
	}

	@Override
	public HttpStatusCode getStatusHttp() {
		return statusHttp;
	}

}