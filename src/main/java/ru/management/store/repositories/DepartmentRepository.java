package ru.management.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.management.store.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}