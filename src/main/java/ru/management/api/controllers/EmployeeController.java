package ru.management.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.management.api.EmployeeAPI;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.DBException;
import ru.management.api.exceptions.InvalidDataException;
import ru.management.services.EmployeeService;

import java.util.List;


@RestController
public class EmployeeController implements EmployeeAPI {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) throws DBException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.createEmployee(employeeDTO));
    }

    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() throws DBException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees());
    }

    @Override
    public ResponseEntity<EmployeeDTO> getEmployeeById(String id) throws DBException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<Void> updateEmployeeById(String id, EmployeeDTO employeeDTO) throws DBException, InvalidDataException {
        employeeService.updateEmployeeById(id, employeeDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> deleteEmployeeById(String id) throws DBException {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}