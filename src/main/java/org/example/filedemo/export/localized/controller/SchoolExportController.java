package org.example.filedemo.export.localized.controller;

import lombok.RequiredArgsConstructor;
import org.example.filedemo.export.localized.service.SchoolExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolExportController {

    private final SchoolExportService schoolExportService;

    @GetMapping("/export")
    public ResponseEntity<String> export(@RequestParam String lang) throws IOException {
        String filePath = schoolExportService.exportSchools(lang);
        return ResponseEntity.ok("Saved to: " + filePath);
    }
}
