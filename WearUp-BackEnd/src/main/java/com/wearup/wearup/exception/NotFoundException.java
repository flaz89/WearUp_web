package com.wearup.wearup.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(UUID id) {
		super(id + " non trovato!");
	}
	
	public NotFoundException(Long id) {
		super(id + " non trovato!");
	}
}
