package ru.management.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.management.api.EmployeeAPI;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.DBAccessException;
import ru.management.api.exceptions.NotFoundException;
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
    public ResponseEntity<EmployeeDTO> createEmployee(EmployeeDTO employeeDTO) throws DBAccessException, IllegalArgumentException {
        try {
            EmployeeDTO employee = employeeService.createEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.OK).body(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DBAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @Override
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees());
        } catch (DBAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @Override
    public ResponseEntity<EmployeeDTO> getEmployeeById(String id) {
        try {
            long employeeId = Long.parseLong(id);
            return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(employeeId));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DBAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Void> updateEmployeeById(String id, EmployeeDTO employeeDTO) {
        try {
            long employeeId = Long.parseLong(id);
            employeeService.updateEmployeeById(employeeId, employeeDTO);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DBAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteEmployeeById(String id) {
        try {
            long employeeId = Long.parseLong(id);
            employeeService.deleteEmployeeById(employeeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DBAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}