package org.example.filedemo.export.simple.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private final Path baseDir = Path.of("exports");

    public FileStorageService() throws IOException {
        Files.createDirectories(baseDir);
    }

    public Path resolvePath(String fileName) {
        return baseDir.resolve(fileName);
    }
}

