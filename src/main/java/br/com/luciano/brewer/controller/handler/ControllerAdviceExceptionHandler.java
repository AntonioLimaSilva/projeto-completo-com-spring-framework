package br.com.luciano.brewer.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.luciano.brewer.service.exception.NegocioException;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<String> handlerNomeEstiloJaCadastroException(NegocioException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
