package ru.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.entity.EmployeeDepartment;

public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartment, Long> {
}
