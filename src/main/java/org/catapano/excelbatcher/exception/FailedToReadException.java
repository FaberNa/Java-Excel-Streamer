package org.catapano.excelbatcher.exception;

public class FailedToReadException extends RuntimeException{

    public FailedToReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
