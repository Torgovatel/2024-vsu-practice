package ru.management.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.management.api.dto.EmployeeDTO;

import java.util.List;

@RequestMapping("/")
public interface EmployeeAPI {
    @PostMapping("/employee")
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO);

    @GetMapping("/employees")
    ResponseEntity<List<EmployeeDTO>> getAllEmployees();

    @GetMapping("/employee/{id}")
    ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id);

    @PutMapping("/employee/{id}")
    ResponseEntity<Void> updateEmployeeById(@PathVariable String id, @RequestBody EmployeeDTO employeeDTO);

    @DeleteMapping("/employee/{id}")
    ResponseEntity<Void> deleteEmployeeById(@PathVariable String id);
}