package org.example.springdynamodb.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import org.example.springdynamodb.dto.EmployeeDTO;
import org.example.springdynamodb.dto.ResponseDTO;
import org.example.springdynamodb.entity.Employee;
import org.example.springdynamodb.exception.EmployeeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final DynamoDBMapper dynamoDBMapper;

    @Override
    public ResponseDTO<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO) {
        log.info("employeeDTO => {}", employeeDTO);
        if (ObjectUtils.isEmpty(employeeDTO)) {
            throw new EmployeeException("Employee details cannot be null");
        }
        Employee employee = new Employee(employeeDTO.id(), employeeDTO.name(),  employeeDTO.phone(), employeeDTO.email());
        dynamoDBMapper.save(employee);
        return new ResponseDTO<>("Employee created successfully", new EmployeeDTO(employee.getId(), employee.getName(), employee.getPhone(), employee.getEmail()));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employeeList = dynamoDBMapper.scan(Employee.class, new DynamoDBScanExpression());
        log.info("Employee list count => {}", employeeList.size());
        return employeeList.stream().map(e -> new EmployeeDTO(e.getId(), e.getName(), e.getPhone(), e.getEmail())).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployee(String id) {
        log.info("Employee id => {}", id);
        if (!StringUtils.hasLength(id)) {
            throw new EmployeeException("Employee id cannot be null");
        }
        Employee employee = dynamoDBMapper.load(Employee.class, id);
        return new EmployeeDTO(employee.getId(), employee.getName(),employee.getPhone(), employee.getEmail());
    }

    @Override
    public ResponseDTO<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO) {
        log.info("employeeDTO => {}", employeeDTO);
        if (ObjectUtils.isEmpty(employeeDTO)) {
            throw new EmployeeException("Employee details cannot be null");
        }
        Employee employee = new Employee(employeeDTO.id(), employeeDTO.name(), employeeDTO.phone(), employeeDTO.email());
        dynamoDBMapper.save(employee, buildExpression(employee));
        return new ResponseDTO<>("Employee updated successfully", new EmployeeDTO(employee.getId(), employee.getName(), employee.getPhone(), employee.getEmail()));
    }

    @Override
    public ResponseDTO<EmployeeDTO> deleteEmployee(String id) {
        log.info("Employee id => {}", id);
        if (!StringUtils.hasLength(id)) {
            throw new EmployeeException("Employee id cannot be null");
        }
        Employee employee = dynamoDBMapper.load(Employee.class, id);
        if (ObjectUtils.isEmpty(employee)) {
            throw new EmployeeException("No data found");
        }
        dynamoDBMapper.delete(employee);
        return new ResponseDTO<>("Employee deleted successfully", null);
    }

    private DynamoDBSaveExpression buildExpression(Employee employee) {
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("id", new ExpectedAttributeValue(new AttributeValue().withS(employee.getId())));
        dynamoDBSaveExpression.setExpected(expectedAttributeValueMap);
        return dynamoDBSaveExpression;
    }
}
