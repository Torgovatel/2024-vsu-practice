package ru.management.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.management.api.dto.EmployeeDTO;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestDataLoader {
    public static TestData loadTestDataFromFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Настройка ObjectMapper для игнорирования неизвестных свойств
        File testDataFile = new File(filePath);
        return objectMapper.readValue(testDataFile, TestData.class);
    }

    public static class TestData {
        private List<EmployeeDTO> valid;
        private List<EmployeeDTO> invalid;

        public List<EmployeeDTO> getValid() {
            return valid;
        }

        public void setValid(List<EmployeeDTO> valid) {
            this.valid = valid;
        }

        public List<EmployeeDTO> getInvalid() {
            return invalid;
        }

        public void setInvalid(List<EmployeeDTO> invalid) {
            this.invalid = invalid;
        }
    }
}
