package ru.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.DBException;
import ru.management.api.exceptions.InvalidDataException;
import ru.management.api.exceptions.NotFoundException;
import ru.management.store.entities.Employee;
import ru.management.store.repositories.EmployeeRepository;
import ru.management.util.converters.EmployeeConverter;

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

    private boolean isNotValidEmployeeDTO(EmployeeDTO employeeDTO) {
        Errors errors = new BeanPropertyBindingResult(employeeDTO, "employeeDTO");
        validator.validate(employeeDTO, errors);
        return errors.hasErrors();
    }

    public List<EmployeeDTO> getAllEmployees()
            throws DBException {
        try {
            List<Employee> employees = employeeRepository.findAll();
            return employees.stream()
                    .map(employeeConverter::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DBException(e.getMessage());
        }
    }

    public EmployeeDTO getEmployeeById(String id)
            throws DBException, NotFoundException {
        try {
            long employeeId = Long.parseLong(id);
            if (employeeId <= 0) {
                throw new NumberFormatException();
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            Employee employee = optionalEmployee.orElseThrow(() -> new NotFoundException("Employee with id " + employeeId + " not found"));
            return employeeConverter.toDTO(employee);
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            } else if (e instanceof NumberFormatException) {
                throw new InvalidDataException("Path variable id must be positive number, not id = " + id);
            }
            throw new DBException(e.getMessage());
        }
    }

    @Transactional
    public void updateEmployeeById(String id, EmployeeDTO employeeDTO)
            throws DBException, InvalidDataException, NotFoundException {
        try {
            long employeeId = Long.parseLong(id);
            if (employeeId <= 0) {
                throw new NumberFormatException();
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            Employee existingEmployee = optionalEmployee.orElseThrow(() ->
                    new NotFoundException("Employee with id " + employeeId + " not found"));
            employeeDTO = EmployeeDTO.builder()
                    .id(existingEmployee.getId())
                    .name(Objects.requireNonNullElse(employeeDTO.getName(), existingEmployee.getName()))
                    .surname(Objects.requireNonNullElse(employeeDTO.getSurname(), existingEmployee.getSurname()))
                    .passportNumber(Objects.requireNonNullElse(employeeDTO.getPassportNumber(), existingEmployee.getPassportNumber()))
                    .passportDate(Objects.requireNonNullElse(employeeDTO.getPassportDate(), existingEmployee.getPassportDate()))
                    .salary(Objects.requireNonNullElse(employeeDTO.getSalary(), existingEmployee.getSalary()))
                    .build();
            if (isNotValidEmployeeDTO(employeeDTO)) {
                throw new InvalidDataException(employeeDTO.toString() + " is no valid employee");
            }
            existingEmployee.setName(Objects.requireNonNullElse(employeeDTO.getName(), existingEmployee.getName()));
            existingEmployee.setSurname(Objects.requireNonNullElse(employeeDTO.getSurname(), existingEmployee.getSurname()));
            existingEmployee.setPassportNumber(Objects.requireNonNullElse(employeeDTO.getPassportNumber(), existingEmployee.getPassportNumber()));
            existingEmployee.setPassportDate(Objects.requireNonNullElse(employeeDTO.getPassportDate(), existingEmployee.getPassportDate()));
            existingEmployee.setSalary(Objects.requireNonNullElse(employeeDTO.getSalary(), existingEmployee.getSalary()));
            employeeRepository.save(existingEmployee);
        } catch (Exception e) {
            if (e instanceof NotFoundException || e instanceof InvalidDataException) {
                throw e;
            } else if (e instanceof NumberFormatException) {
                throw new InvalidDataException("Path variable id must be positive number, not id = " + id);
            }
            throw new DBException(e.getMessage());
        }
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO)
            throws DBException, InvalidDataException {
        try {
            if (isNotValidEmployeeDTO(employeeDTO)) {
                throw new InvalidDataException(employeeDTO.toString() + " is no valid employee");
            }
            Employee newEmployee = employeeRepository.save(employeeConverter.toEntity(employeeDTO));
            return employeeConverter.toDTO(newEmployee);
        } catch (Exception e) {
            if (e instanceof InvalidDataException) {
                throw e;
            }
            throw new DBException(e.getMessage());
        }
    }

    @Transactional
    public void deleteEmployeeById(String id) throws DBException, NotFoundException {
        try {
            long employeeId = Long.parseLong(id);
            if (employeeId <= 0) {
                throw new NumberFormatException();
            }
            Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
            Employee employee = optionalEmployee.orElseThrow(() ->
                    new NotFoundException("Employee with id " + employeeId + " not found"));
            employeeRepository.deleteById(employeeId);
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            } else if (e instanceof NumberFormatException) {
                throw new InvalidDataException("Path variable id must be positive number, not id = " + id);
            }
            throw new DBException(e.getMessage());
        }
    }
}