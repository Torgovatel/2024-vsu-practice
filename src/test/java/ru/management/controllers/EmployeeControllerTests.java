package ru.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.management.api.controllers.EmployeeController;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.DBAccessException;
import ru.management.api.exceptions.NotFoundException;
import ru.management.services.EmployeeService;
import ru.management.utils.TestDataLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeControllerTest {
    private EmployeeService employeeService;
    private EmployeeController employeeController;
    private TestDataLoader.TestData testData;

    @BeforeEach
    void setUp() throws IOException {
        employeeService = mock(EmployeeService.class);
        employeeController = new EmployeeController(employeeService);
        String jsonContent = Files.readString(Paths.get("src/test/resources/employeeControllerTests.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        testData = objectMapper.readValue(jsonContent, TestDataLoader.TestData.class);
    }
    @Test
    void getAll_ReturnsListOfEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(testData.getValid());
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testData.getValid(), response.getBody());
        verify(employeeService).getAllEmployees();
    }
    @Test
    void getById_ValidId_ReturnsEmployee() {
        testData.getValid().forEach(validEmployeeDTO -> {
            long id = validEmployeeDTO.getId();
            when(employeeService.getEmployeeById(id)).thenReturn(validEmployeeDTO);
            ResponseEntity<EmployeeDTO> response = employeeController.getById(Long.toString(id));
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(validEmployeeDTO, response.getBody());
            verify(employeeService).getEmployeeById(id);
        });
    }
    @Test
    void getById_InvalidId_ReturnsBadRequest() {
        String invalidId = "abc";
        ResponseEntity<EmployeeDTO> response = employeeController.getById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService, never()).getEmployeeById(anyLong());
        invalidId = "-2";
        doThrow(NumberFormatException.class).when(employeeService).getEmployeeById(anyLong());
        response = employeeController.getById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService).getEmployeeById(Long.parseLong(invalidId));
        invalidId = "0";
        doThrow(NumberFormatException.class).when(employeeService).getEmployeeById(anyLong());
        response = employeeController.getById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService).getEmployeeById(Long.parseLong(invalidId));
    }

    @Test
    void getById_NonExistingId_ReturnsNotFound() {
        long nonExistingId = 999L;
        doThrow(new NotFoundException("Employee with id " + nonExistingId + " not found")).when(employeeService).getEmployeeById(nonExistingId);
        ResponseEntity<EmployeeDTO> response = employeeController.getById(Long.toString(nonExistingId));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeService).getEmployeeById(nonExistingId);
    }
    @Test
    void updateById_ValidData_ReturnsNoContent() {
        testData.getValid().forEach(validEmployeeDTO -> {
            long id = validEmployeeDTO.getId();
            doNothing().when(employeeService).updateEmployeeById(eq(id), any(EmployeeDTO.class));
            ResponseEntity<Void> response = employeeController.updateById(Long.toString(id), validEmployeeDTO);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(employeeService).updateEmployeeById(id, validEmployeeDTO);
        });
    }

    @Test
    void updateById_InvalidData_ReturnsBadRequest() {
        testData.getInvalid().forEach(invalidEmployeeDTO -> {
            long id = invalidEmployeeDTO.getId();
            doThrow(IllegalArgumentException.class).when(employeeService).updateEmployeeById(eq(id), any(EmployeeDTO.class));
            ResponseEntity<Void> response = employeeController.updateById(Long.toString(id), invalidEmployeeDTO);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(employeeService).updateEmployeeById(id, invalidEmployeeDTO);
        });
    }
    @Test
    void updateById_NonExistingId_ReturnsNotFound() {
        long nonExistingId = 999L;
        EmployeeDTO updatedEmployee = testData.getInvalid().get(0);
        updatedEmployee.setId(nonExistingId);
        doThrow(NotFoundException.class).when(employeeService).updateEmployeeById(eq(nonExistingId), any(EmployeeDTO.class));
        ResponseEntity<Void> response = employeeController.updateById(Long.toString(nonExistingId), updatedEmployee);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeService).updateEmployeeById(nonExistingId, updatedEmployee);
    }
    @Test
    void deleteById_ValidId_ReturnsNoContent() {
        testData.getValid().stream().map(EmployeeDTO::getId).forEach(id -> {
            doNothing().when(employeeService).deleteEmployeeById(id);
            ResponseEntity<Void> response = employeeController.deleteById(Long.toString(id));
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(employeeService).deleteEmployeeById(id);
        });
    }
    @Test
    void deleteById_InvalidId_ReturnsBadRequest() {
        String invalidId = "abc";
        ResponseEntity<Void> response = employeeController.deleteById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService, never()).deleteEmployeeById(anyLong());
        invalidId = "-2";
        doThrow(NumberFormatException.class).when(employeeService).deleteEmployeeById(anyLong());
        response = employeeController.deleteById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService).deleteEmployeeById(Long.parseLong(invalidId));
        invalidId = "0";
        doThrow(NumberFormatException.class).when(employeeService).deleteEmployeeById(anyLong());
        response = employeeController.deleteById(invalidId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(employeeService).deleteEmployeeById(Long.parseLong(invalidId));
    }

    @Test
    void deleteById_NonExistingId_ReturnsNotFound() throws NotFoundException, DBAccessException {
        long nonExistingId = 999L;
        doThrow(NotFoundException.class).when(employeeService).deleteEmployeeById(nonExistingId);
        ResponseEntity<Void> response = employeeController.deleteById(Long.toString(nonExistingId));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(employeeService).deleteEmployeeById(nonExistingId);
    }

}