package org.example.springdynamodb.service;

import org.example.springdynamodb.dto.EmployeeDTO;
import org.example.springdynamodb.dto.ResponseDTO;

import java.util.List;

public interface EmployeeService {
    ResponseDTO<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployee(String id);

    ResponseDTO<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO);

    ResponseDTO<EmployeeDTO> deleteEmployee(String id);
}
