package com.CloudStore.exceptions;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String message) {
        super(message);
    }
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public FileNotFoundException(Class<?> entityType, String field, Object property) {
        super(entityType.getSimpleName() + " wasnt found with "+field+" : " + property);
    }
}
