package br.com.luciano.brewer.service.exception;

public class IlegalSenhaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IlegalSenhaException(String message) {
		super(message);
	}

}
