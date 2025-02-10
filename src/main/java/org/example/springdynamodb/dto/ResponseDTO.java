package org.example.springdynamodb.dto;

public record ResponseDTO<T>(String message, T content) {
}
