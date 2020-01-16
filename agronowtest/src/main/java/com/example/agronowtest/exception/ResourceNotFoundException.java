package com.example.agronowtest.exception;

public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = -3254201331041110122L;

	public ResourceNotFoundException() {
    }
 
    public ResourceNotFoundException(String msg) {
        super(msg);
    }  
}
