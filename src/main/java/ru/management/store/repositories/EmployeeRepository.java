package ru.management.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.store.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
