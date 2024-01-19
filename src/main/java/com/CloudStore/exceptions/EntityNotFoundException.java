package com.CloudStore.exceptions;

import java.lang.reflect.Field;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public EntityNotFoundException(Class<?> entityType, String field, Object property) {
        super(entityType.getSimpleName() + " wasnt found with "+field+" : " + property);
    }
}
