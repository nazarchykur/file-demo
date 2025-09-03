package org.example.filedemo.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class CsvReportExporter<T> implements ReportExporter<T> {
    private final CsvMapper csvMapper;

    public CsvReportExporter() {
        this.csvMapper = CsvMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Override
    public void export(List<T> data, Path path) throws IOException {
        if (data.isEmpty()) throw new IllegalArgumentException("No data to export");
        CsvSchema schema = csvMapper.schemaFor(data.getFirst().getClass())
                .withHeader()
                .withColumnReordering(true);
        csvMapper.writer(schema).writeValue(path.toFile(), data);
    }
}

