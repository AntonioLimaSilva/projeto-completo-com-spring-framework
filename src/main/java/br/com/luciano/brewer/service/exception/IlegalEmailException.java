package br.com.luciano.brewer.service.exception;

public class IlegalEmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IlegalEmailException(String message) {
		super(message);
	}

}
