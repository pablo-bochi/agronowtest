package com.example.agronowtest.exception;

public class ResourceAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -8485988581382600293L;

	public ResourceAlreadyExistsException() {
    }
 
    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }
	
}
