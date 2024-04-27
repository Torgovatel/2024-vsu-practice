package ru.management.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.management.dto.EmployeeDTO;

import java.util.List;

@RequestMapping("/default")
public interface EmployeeAPI {
    @PostMapping
    ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO employeeDTO);

    @GetMapping("/all")
    ResponseEntity<List<EmployeeDTO>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<EmployeeDTO> getById(@PathVariable String id);

    @PutMapping("/{id}")
    ResponseEntity<Void> updateById(@PathVariable String id, @RequestBody EmployeeDTO employeeDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable String id);
}