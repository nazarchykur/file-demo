package org.example.filedemo.export.simple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.filedemo.export.simple.dto.AddressDto;
import org.example.filedemo.export.simple.dto.EmployeeCsvDto;
import org.example.filedemo.export.simple.dto.EmployeeDto;
import org.example.filedemo.export.simple.dto.EmployeeListXmlDto;
import org.example.filedemo.export.simple.dto.EmployeeXmlDto;
import org.example.filedemo.export.simple.service.CsvReportExporter;
import org.example.filedemo.export.simple.service.FileStorageService;
import org.example.filedemo.export.simple.service.JsonReportExporter;
import org.example.filedemo.export.simple.service.XmlReportExporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final JsonReportExporter<EmployeeDto> jsonExporter;
    private final CsvReportExporter<EmployeeCsvDto> csvExporter;
    private final XmlReportExporter<EmployeeListXmlDto> xmlExporter;
    private final FileStorageService storageService;
    ObjectMapper mapper = new ObjectMapper();


    @PostMapping("/{format}")
    public ResponseEntity<String> export(@PathVariable String format) throws IOException {
        // Дані для JSON / XML
        var dataJson = List.of(
                new EmployeeDto(
                        UUID.randomUUID(),
                        "Alice",
                        30,
                        List.of("Java", "Spring Boot"),
                        new AddressDto("Main St", "Kyiv", "UA"),
                        LocalDate.now(),
                        LocalDateTime.now(),
                        Map.of("level", "senior", "remote", true)
                )
        );

        // Дані для CSV (плоска структура)
        var dataCsv = List.of(
                new EmployeeCsvDto(
                        UUID.randomUUID(),
                        "Alice",
                        30,
                        List.of("Java", "Spring Boot"),
                        "Main St",
                        "Kyiv",
                        "UA",
                        LocalDate.now(),
                        LocalDateTime.now(),
                        mapper.writeValueAsString(Map.of("level", "senior", "remote", true)) // ✅ JSON у колонці
                )
        );

        // Дані для XML (свої DTO)
        var dataXml = new EmployeeListXmlDto(
                List.of(
                        new EmployeeXmlDto(
                                UUID.randomUUID(),
                                "Alice",
                                30,
                                List.of("Java", "Spring Boot"),
                                new AddressDto("Main St", "Kyiv", "UA"),
                                LocalDate.now(),
                                LocalDateTime.now(),
                                Map.of("level", "senior", "remote", true)
                        )
                )
        );

        String fileExtension = switch (format.toLowerCase()) {
            case "json", "csv", "xml" -> format.toLowerCase();
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };

        String fileName = "employee-" + format + "." + fileExtension;
        Path path = storageService.resolvePath(fileName);

        switch (format.toLowerCase()) {
            case "json" -> jsonExporter.export(dataJson, path);
            case "csv"  -> csvExporter.export(dataCsv, path);
            case "xml"  -> xmlExporter.export(dataXml, path);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        }

        return ResponseEntity.ok("Saved to: " + path.toAbsolutePath());
    }
}