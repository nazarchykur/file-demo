package org.example.filedemo.export.simple.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record EmployeeDto(
        UUID id,
        String name,
        int age,
        List<String> skills,
        AddressDto address,
        LocalDate hireDate,
        LocalDateTime lastLogin,
        Map<String, Object> metadata
) {
}

