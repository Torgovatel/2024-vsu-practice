package ru.torgovatel.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Добавление нового сотрудника
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeRepository.save(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    // Изменение зарплаты сотрудника
    @PatchMapping("/{id}/salary")
    public ResponseEntity<Employee> updateEmployeeSalary(@PathVariable Long id, @RequestBody Double newSalary) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setSalary(newSalary);
        employeeRepository.save(employee);
        return ResponseEntity.ok(employee);
    }

    // Изменение паспортных данных сотрудника
    @PatchMapping("/{id}/passport")
    public ResponseEntity<Employee> updateEmployeePassport(@PathVariable Long id, @RequestBody Employee passportData) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setPassportNumber(passportData.getPassportNumber());
        employee.setPassportDate(passportData.getPassportDate());
        employeeRepository.save(employee);
        return ResponseEntity.ok(employee);
    }

    // Получение сотрудника по id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return ResponseEntity.ok(employee);
    }
}
