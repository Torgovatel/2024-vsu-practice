package ru.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import ru.management.dto.EmployeeDTO;
import ru.management.entity.Employee;
import ru.management.exception.DBAccessException;
import ru.management.exception.NotFoundException;
import ru.management.repository.EmployeeRepository;
import ru.management.util.converter.EmployeeConverter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;
    private final LocalValidatorFactoryBean validator;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeConverter employeeConverter,
                           LocalValidatorFactoryBean validator) {
        this.employeeRepository = employeeRepository;
        this.employeeConverter = employeeConverter;
        this.validator = validator;
    }

    private boolean isValidEmployeeDTO(EmployeeDTO employeeDTO) {
        Errors errors = new BeanPropertyBindingResult(employeeDTO, "employeeDTO");
        validator.validate(employeeDTO, errors);
        return !errors.hasErrors();
    }

    public List<EmployeeDTO> getAllEmployees() throws DBAccessException {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees.stream()
                    .map(employeeConverter::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBAccessException(e.getMessage());
        }
    }

    public EmployeeDTO getEmployeeById(long id) throws DBAccessException, NotFoundException, NumberFormatException {
        try {
            if (id <= 0) {
                throw new NumberFormatException("id must be positive number");
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            Employee employee = optionalEmployee.orElseThrow(() -> new NotFoundException("Employee with id " + id + " not found"));
            return employeeConverter.toDTO(employee);
        } catch (Exception e) {
            if (e instanceof NotFoundException || e instanceof NumberFormatException) {
                throw e;
            } else {
                throw new DBAccessException(e.getMessage());
            }
        }
    }

    @Transactional
    public void updateEmployeeById(long id, EmployeeDTO employeeDTO)
            throws DBAccessException, NotFoundException, IllegalArgumentException {
        try {
            if (id <= 0) {
                throw new NumberFormatException("id must be positive number");
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            Employee existingEmployee = optionalEmployee.orElseThrow(() -> new NotFoundException("Employee with id " + id + " not found"));
            employeeDTO = EmployeeDTO.builder()
                    .id(existingEmployee.getId())
                    .name(Objects.requireNonNullElse(employeeDTO.getName(), existingEmployee.getName()))
                    .surname(Objects.requireNonNullElse(employeeDTO.getSurname(), existingEmployee.getSurname()))
                    .passportNumber(Objects.requireNonNullElse(employeeDTO.getPassportNumber(), existingEmployee.getPassportNumber()))
                    .passportDate(Objects.requireNonNullElse(employeeDTO.getPassportDate(), existingEmployee.getPassportDate()))
                    .salary(Objects.requireNonNullElse(employeeDTO.getSalary(), existingEmployee.getSalary()))
                    .build();
            if(!isValidEmployeeDTO(employeeDTO)) {
                throw new IllegalArgumentException(employeeDTO.toString() + " is no valid employee");
            }
            existingEmployee.setName(Objects.requireNonNullElse(employeeDTO.getName(), existingEmployee.getName()));
            existingEmployee.setSurname(Objects.requireNonNullElse(employeeDTO.getSurname(), existingEmployee.getSurname()));
            existingEmployee.setPassportNumber(Objects.requireNonNullElse(employeeDTO.getPassportNumber(), existingEmployee.getPassportNumber()));
            existingEmployee.setPassportDate(Objects.requireNonNullElse(employeeDTO.getPassportDate(), existingEmployee.getPassportDate()));
            existingEmployee.setSalary(Objects.requireNonNullElse(employeeDTO.getSalary(), existingEmployee.getSalary()));
            employeeRepository.save(existingEmployee);
        } catch (Exception e) {
            if (e instanceof NotFoundException || e instanceof IllegalArgumentException) {
                throw e;
            } else {
                throw new DBAccessException(e.getMessage());
            }
        }
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) throws DBAccessException, IllegalArgumentException {
        try {
            if(!isValidEmployeeDTO(employeeDTO)) {
                throw new IllegalArgumentException(employeeDTO.toString() + " is no valid employee");
            }
            Employee newEmployee = employeeRepository.save(employeeConverter.toEntity(employeeDTO));
            return employeeConverter.toDTO(newEmployee);
        } catch (Exception e) {
            throw new DBAccessException(e.getMessage());
        }
    }

    @Transactional
    public void deleteEmployeeById(long id) throws DBAccessException, NotFoundException, NumberFormatException {
        try {
            if (id <= 0) {
                throw new NumberFormatException("id must be positive number");
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            Employee employee = optionalEmployee.orElseThrow(() -> new NotFoundException("Employee with id " + id + " not found"));
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof NotFoundException || e instanceof NumberFormatException) {
                throw e;
            }
            throw new DBAccessException(e.getMessage());
        }
    }
}