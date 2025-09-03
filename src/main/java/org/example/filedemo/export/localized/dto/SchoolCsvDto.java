package org.example.filedemo.export.localized.dto;

public record SchoolCsvDto(
        Long id,
        String name,
        String city,
        Integer studentsCount
) {}

