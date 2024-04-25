package ru.management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employees_departments")
@Data
public class EmployeeDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
