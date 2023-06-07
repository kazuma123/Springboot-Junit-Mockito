package org.app.springboot.test.exceptions;

public class DineroInsuficienteException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DineroInsuficienteException(String message) {
		super(message);
	}
	
}
