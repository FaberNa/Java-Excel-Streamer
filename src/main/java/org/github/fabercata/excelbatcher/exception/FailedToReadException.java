package org.github.fabercata.excelbatcher.exception;

public class FailedToReadException extends RuntimeException{

    public FailedToReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
