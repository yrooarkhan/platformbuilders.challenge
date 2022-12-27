package io.platformbuilders.challenge.infrastructure.exception.template;

import org.springframework.http.HttpStatusCode;

public interface ErroConhecido {

	HttpStatusCode getStatusHttp();

	String getMessage();

}
