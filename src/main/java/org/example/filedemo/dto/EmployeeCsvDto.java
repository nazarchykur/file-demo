package org.example.filedemo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


// need to specify order of fields in CSV file, otherwise they will be in alphabetic order by default
@JsonPropertyOrder({
        "id", "name", "age", "skills",
        "street", "city", "country",
        "hireDate", "lastLogin", "metadata"
})
public record EmployeeCsvDto(
        UUID id,
        String name,
        int age,
        List<String> skills,
        String street,
        String city,
        String country,
        LocalDate hireDate,
        LocalDateTime lastLogin,
        String metadata // тут зберігається JSON як String
) {
}

