package edu.iris.dmc.validation.validator;

public class InvalidTypeOperation extends Exception {

	public InvalidTypeOperation() {

	}

	public InvalidTypeOperation(String message) {
		super(message);
	}

	public InvalidTypeOperation(Exception e) {
		super(e);
	}
}
