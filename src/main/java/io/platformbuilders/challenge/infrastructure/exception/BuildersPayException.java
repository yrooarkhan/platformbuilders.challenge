package io.platformbuilders.challenge.infrastructure.exception;

import org.springframework.http.HttpStatusCode;

public abstract class BuildersPayException extends Exception {

	private static final long serialVersionUID = -8467687632367919878L;

	private final HttpStatusCode statusHttp;

	protected BuildersPayException(HttpStatusCode statusHttp, String descricaoErro) {
		super(descricaoErro);
		this.statusHttp = statusHttp;
	}

	public HttpStatusCode getStatusHttp() {
		return statusHttp;
	}

}
