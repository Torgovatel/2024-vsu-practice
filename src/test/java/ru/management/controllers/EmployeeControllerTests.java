package ru.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.test.web.servlet.MockMvc;
import ru.management.api.controllers.EmployeeController;
import ru.management.api.dto.EmployeeDTO;
import ru.management.api.exceptions.NotFoundException;
import ru.management.services.EmployeeService;
import ru.management.store.entities.Employee;
import ru.management.store.repositories.EmployeeRepository;
import ru.management.utils.TestDataLoader;

import javax.swing.text.html.parser.Entity;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
    @MockBean
    private EmployeeRepository employeeRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeController employeeController;
    private TestDataLoader testDataLoader;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() throws Exception {
        this.testDataLoader = new TestDataLoader();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        this.objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
    }

    @Test
    void getAllEmployees() throws Exception {
        // valid data
        when(employeeRepository.findAll()).thenReturn(testDataLoader.getValidEntities());
        String expectedJson = objectMapper.writeValueAsString(testDataLoader.getValidDTO());
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedJson));
        // db connection failed
        when(employeeRepository.findAll()).thenThrow(CannotGetJdbcConnectionException.class);
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(503));
    }

    @Test
    void getEmployeeById() throws Exception {
        // valid data
        for (int index = 0; index < testDataLoader.getValidDTO().size(); index++) {
            EmployeeDTO currentEmployee = testDataLoader.getValidDTO().get(index);
            when(employeeRepository.findById(currentEmployee.getId()))
                    .thenReturn(Optional.of(testDataLoader.getValidEntities().get(index)));
            String expectedJson = objectMapper.writeValueAsString(currentEmployee);
            mockMvc.perform(get("/employee/{id}", currentEmployee.getId()))
                    .andExpect(status().is(200))
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedJson));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).findById(anyLong());
        // db connection failed
        final long employeeId = 999L;
        when(employeeRepository.findById(employeeId)).thenThrow(CannotGetJdbcConnectionException.class);
        mockMvc.perform(get("/employee/{id}", employeeId))
                .andExpect(status().is(503));
        verify(employeeRepository).findById(employeeId);
        // not found
        final long notFoundEmployeeId = 888L;
        when(employeeRepository.findById(notFoundEmployeeId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/employee/{id}", notFoundEmployeeId))
                .andExpect(status().is(404));
        verify(employeeRepository).findById(notFoundEmployeeId);
        // invalid id
        final String[] invalidEmployeeIdArr = {"-1", "0", "abc"};
        for (String invalidId : invalidEmployeeIdArr) {
            mockMvc.perform(get("/employee/{id}", invalidId))
                    .andExpect(status().is(400));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size() + 2)).findById(anyLong());
    }

    @Test
    void createEmployeeById() throws Exception {
        // valid data
        for (int index = 0; index < testDataLoader.getValidDTO().size(); index++) {
            Employee currentEmployee = testDataLoader.getValidEntities().get(index);
            EmployeeDTO currentDTO = testDataLoader.getValidDTO().get(index);
            when(employeeRepository.saveAndFlush(any(Employee.class)))
                    .thenAnswer(invocation -> {
                        Employee savedEmployee = invocation.getArgument(0);
                        savedEmployee.setId(currentDTO.getId());
                        return savedEmployee;
                    });
            String expectedJson = objectMapper.writeValueAsString(currentDTO);
            String requestJson = expectedJson.replaceFirst("\"id\":\\d*,", "");
            mockMvc.perform(post("/employee")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(200))
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(expectedJson));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any());
        // invalid data
        for (int index = 0; index < testDataLoader.getInvalidDTO().size(); index++) {
            EmployeeDTO currentDTO = testDataLoader.getInvalidDTO().get(index);
            String expectedJson = objectMapper.writeValueAsString(currentDTO);
            String requestJson = expectedJson.replaceFirst("\"id\":\\d*,", "");
            mockMvc.perform(post("/employee")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(400));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any());
        // db connection failed
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenThrow(CannotGetJdbcConnectionException.class);
        String requestJson = objectMapper.writeValueAsString(testDataLoader.getValidDTO().get(0))
                .replaceFirst("\"id\":\\d*,", "");
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(503));
    }

    @Test
    void updateEmployeeById() throws Exception {
        // valid data
        for (int index = 0; index < testDataLoader.getValidDTO().size(); index++) {
            EmployeeDTO currentEmployee = testDataLoader.getValidDTO().get(index);
            Employee currentEntity = testDataLoader.getValidEntities().get(index);
            when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(currentEntity);
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(currentEntity));
            String requestJson = objectMapper.writeValueAsString(currentEmployee)
                    .replaceFirst("\"id\":\\d*,", "");
            mockMvc.perform(put("/employee/{id}", currentEmployee.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(204));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any(Employee.class));
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).findById(anyLong());
        // invalid data
        for (EmployeeDTO currentEmployee : testDataLoader.getInvalidDTO()) {
            String requestJson = objectMapper.writeValueAsString(currentEmployee)
                    .replaceFirst("\"id\":\\d*,", "");
            mockMvc.perform(put("/employee/{id}", currentEmployee.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(400));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any(Employee.class));
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).findById(anyLong());
        // invalid id
        String testRequestJson = objectMapper.writeValueAsString(testDataLoader.getValidDTO().get(0))
                .replaceFirst("\"id\":\\d*,", "");
        String[] invalidIdArr = {"-1", "0", "abc"};
        for (String invalidId : invalidIdArr) {
            mockMvc.perform(put("/employee/{id}", invalidId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testRequestJson))
                    .andExpect(status().is(400));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any(Employee.class));
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).findById(anyLong());
        // not found
        for (int index = 0; index < testDataLoader.getValidDTO().size(); index++) {
            EmployeeDTO currentEmployee = testDataLoader.getValidDTO().get(index);
            Employee currentEntity = testDataLoader.getValidEntities().get(index);
            when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(currentEntity);
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
            String requestJson = objectMapper.writeValueAsString(currentEmployee)
                    .replaceFirst("\"id\":\\d*,", "");
            mockMvc.perform(put("/employee/{id}", currentEmployee.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().is(404));
        }
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any(Employee.class));
        verify(employeeRepository, times(2*testDataLoader.getValidDTO().size())).findById(anyLong());
        // db connection failed
        when(employeeRepository.findById(anyLong())).thenThrow(CannotGetJdbcConnectionException.class);
        EmployeeDTO testEmployee = testDataLoader.getValidDTO().get(0);
        String requestJson = objectMapper.writeValueAsString(testEmployee)
                .replaceFirst("\"id\":\\d*,", "");
        final long validId = testEmployee.getId();
        mockMvc.perform(put("/employee/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(503));
        verify(employeeRepository, times(testDataLoader.getValidDTO().size())).saveAndFlush(any(Employee.class));
        verify(employeeRepository, times(1 + 2*testDataLoader.getValidDTO().size())).findById(anyLong());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        // valid data
        Employee testEmployee = testDataLoader.getValidEntities().get(0);
        final long validId = testEmployee.getId();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(testEmployee));
        mockMvc.perform(delete("/employee/{id}", validId))
                .andExpect(status().is(204));
        verify(employeeRepository, times(1)).findById(anyLong());
        // invalid id
        final String[] invalidIdArr = {"-1", "0", "abc"};
        for (String invalidId: invalidIdArr) {
            mockMvc.perform(delete("/employee/{id}", invalidId))
                    .andExpect(status().is(400));
        }
        verify(employeeRepository, times(1)).findById(anyLong());
        // not found
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(delete("/employee/{id}", validId))
                .andExpect(status().is(404));
        verify(employeeRepository, times(2)).findById(anyLong());
        // db connection failed
        when(employeeRepository.findById(anyLong())).thenThrow(CannotGetJdbcConnectionException.class);
        mockMvc.perform(delete("/employee/{id}", validId))
                .andExpect(status().is(503));
        verify(employeeRepository, times(3)).findById(anyLong());
    }
}