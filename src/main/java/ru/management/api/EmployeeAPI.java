package ru.management.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.DBException;
import ru.management.api.exceptions.InvalidDataException;

import java.util.List;

@RequestMapping("/")
public interface EmployeeAPI {
    @PostMapping("/employee")
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO)
            throws DBException;

    @GetMapping("/employees")
    ResponseEntity<List<EmployeeDTO>> getAllEmployees()
            throws DBException;

    @GetMapping("/employee/{id}")
    ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id)
            throws DBException;

    @PutMapping("/employee/{id}")
    ResponseEntity<Void> updateEmployeeById(@PathVariable String id, @RequestBody EmployeeDTO employeeDTO)
            throws DBException, InvalidDataException;

    @DeleteMapping("/employee/{id}")
    ResponseEntity<Void> deleteEmployeeById(@PathVariable String id)
            throws DBException;
}