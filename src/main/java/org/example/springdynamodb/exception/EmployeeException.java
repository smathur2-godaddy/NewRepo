package org.example.springdynamodb.exception;

public class EmployeeException extends RuntimeException {
    public EmployeeException() {
        super();
    }
    public EmployeeException(String message) {
        super(message);
    }
}
