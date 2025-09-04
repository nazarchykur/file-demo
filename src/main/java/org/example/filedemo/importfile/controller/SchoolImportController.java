package org.example.filedemo.importfile.controller;

import lombok.RequiredArgsConstructor;
import org.example.filedemo.importfile.service.SchoolImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolImportController {

    private final SchoolImportService schoolImportService;

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        int count = schoolImportService.importFromCsv(file);
        return ResponseEntity.ok("Imported " + count + " schools.");
    }
}

