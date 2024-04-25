package ru.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}