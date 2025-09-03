package org.example.filedemo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JacksonXmlRootElement(localName = "employee")
public record EmployeeXmlDto(
        UUID id,
        String name,
        int age,
        List<String> skills,
        AddressDto address,
        LocalDate hireDate,
        LocalDateTime lastLogin,
        Map<String, Object> metadata
) {}
