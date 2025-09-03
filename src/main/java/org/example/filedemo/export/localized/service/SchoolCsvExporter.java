package org.example.filedemo.export.localized.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.filedemo.export.localized.dto.SchoolCsvDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SchoolCsvExporter {

    private final CsvMapper csvMapper;
    private final SchoolExportTranslationService translationService;

    public SchoolCsvExporter(SchoolExportTranslationService translationService) {
        this.csvMapper = CsvMapper.builder().findAndAddModules().build();
        this.translationService = translationService;
    }

    public void export(List<SchoolCsvDto> data, Path path, Locale locale) throws IOException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("No data to export");
        }

        // 1. Отримуємо локалізовані заголовки
        String[] headers = translationService.getHeaders(locale);

        // 2. Будуємо схему CSV із локалізованими заголовками
        CsvSchema.Builder schemaBuilder = CsvSchema.builder();
        for (String header : headers) {
            schemaBuilder.addColumn(header);
        }
        CsvSchema schema = schemaBuilder.build().withHeader();

        // 3. Мапимо DTO → Map із локалізованими ключами
        List<Map<String, Object>> rows = data.stream()
                .map(dto -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put(headers[0], dto.id());
                    row.put(headers[1], dto.name());
                    row.put(headers[2], dto.city());
                    row.put(headers[3], dto.studentsCount());
                    return row;
                })
                .toList();

        // 4. Записуємо CSV
        csvMapper.writer(schema).writeValue(path.toFile(), rows);
    }
}

