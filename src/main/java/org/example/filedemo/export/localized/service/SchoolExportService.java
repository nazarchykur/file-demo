package org.example.filedemo.export.localized.service;

import lombok.RequiredArgsConstructor;
import org.example.filedemo.export.localized.dto.SchoolCsvDto;
import org.example.filedemo.export.simple.service.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SchoolExportService {

    private final SchoolCsvExporter exporter;
    private final FileStorageService storageService;

    public String exportSchools(String lang) throws IOException {
        Locale locale = resolveLocale(lang);

        // Дані можна підтягувати з БД, зараз — демо
        List<SchoolCsvDto> schools = getDummyData();

        String fileName = "schools-" + lang + ".csv";
        Path path = storageService.resolvePath(fileName);

        exporter.export(schools, path, locale);

        return path.toAbsolutePath().toString();
    }

    private Locale resolveLocale(String lang) {
        return switch (lang.toLowerCase()) {
            case "uk" -> Locale.forLanguageTag("uk");
            case "en" -> Locale.ENGLISH;
            default -> throw new IllegalArgumentException("Unsupported language: " + lang);
        };
    }

    private List<SchoolCsvDto> getDummyData() {
        return List.of(
                new SchoolCsvDto(1L, "Kyiv School #1", "Kyiv", 500),
                new SchoolCsvDto(2L, "Lviv Gymnasium", "Lviv", 350)
        );
    }
}

