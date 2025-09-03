package org.example.filedemo.service;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class XmlReportExporter<T> {
    private final XmlMapper xmlMapper;

    public XmlReportExporter() {
        this.xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public void export(T data, Path path) throws IOException {
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
    }
}
