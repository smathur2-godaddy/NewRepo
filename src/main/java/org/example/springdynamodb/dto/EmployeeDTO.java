package org.example.springdynamodb.dto;

import java.math.BigDecimal;
import java.util.Date;

public record EmployeeDTO(String id, String name, String phone, String email) {
}
