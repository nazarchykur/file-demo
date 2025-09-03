package org.example.filedemo.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ReportExporter<T> {
    void export(List<T> data, Path path) throws IOException;
}

