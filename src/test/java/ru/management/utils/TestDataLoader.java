package ru.management.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import ru.management.api.dto.EmployeeDTO;
import ru.management.store.entities.Employee;
import ru.management.util.converters.EmployeeConverter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TestDataLoader {
    private final String PATH = "src/test/resources/employeeControllerTests.json";
    private List<EmployeeDTO> validDTO;
    private List<EmployeeDTO> invalidDTO;
    private List<Employee> validEntities;
    private List<Employee> invalidEntities;

    public TestDataLoader() throws IOException {
        EmployeeConverter converter = new EmployeeConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File testDataFile = new File(PATH);
        TestData testData = objectMapper.readValue(testDataFile, TestDataLoader.TestData.class);
        this.validDTO = testData.valid;
        this.invalidDTO = testData.invalid;
        this.validEntities = this.validDTO.stream().map(converter::toEntity).collect(Collectors.toList());
        this.invalidEntities = this.invalidDTO.stream().map(converter::toEntity).collect(Collectors.toList());
    }

    private static class TestData {
        public List<EmployeeDTO> valid;
        public List<EmployeeDTO> invalid;
    }
}
