package com.kztp.testengine.exception;

public class InvalidUploadTypeException extends  Exception{
    public InvalidUploadTypeException() {
        super();
    }

    public InvalidUploadTypeException(String message) {
        super(message);
    }
}
