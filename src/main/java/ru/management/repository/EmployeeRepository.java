package ru.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
