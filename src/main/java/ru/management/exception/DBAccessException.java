package ru.management.exception;

import org.springframework.dao.DataAccessException;

public class DBAccessException extends DataAccessException {
    public DBAccessException(String message) {
        super(message);
    }
}