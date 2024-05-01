package ru.management.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.store.entities.EmployeeDepartment;

public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartment, Long> {
}
