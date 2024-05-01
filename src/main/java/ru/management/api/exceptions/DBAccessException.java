package ru.management.api.exceptions;

import org.springframework.dao.DataAccessException;

public class DBAccessException extends DataAccessException {
    public DBAccessException(String message) {
        super(message);
    }
}