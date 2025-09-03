package org.example.filedemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class JsonReportExporter<T> implements ReportExporter<T> {

    private final ObjectMapper objectMapper;

    public JsonReportExporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void export(List<T> data, Path path) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
    }
}
