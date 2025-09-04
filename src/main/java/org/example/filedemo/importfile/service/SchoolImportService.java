package org.example.filedemo.importfile.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.example.filedemo.export.localized.dto.SchoolCsvDto;
import org.example.filedemo.importfile.entity.School;
import org.example.filedemo.importfile.repository.SchoolRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolImportService {

    private final SchoolRepository schoolRepository;
    private final CsvMapper csvMapper = CsvMapper.builder().findAndAddModules().build();

    public int importFromCsv(MultipartFile file) throws IOException {
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        MappingIterator<SchoolCsvDto> it = csvMapper.readerFor(SchoolCsvDto.class)
                .with(schema)
                .readValues(file.getInputStream());

        List<SchoolCsvDto> dtos = it.readAll();

        List<School> entities = dtos.stream()
                .map(dto -> School.builder()
                        .name(dto.name())
                        .city(dto.city())
                        .studentsCount(dto.studentsCount())
                        .build()
                )
                .toList();

        schoolRepository.saveAll(entities);
        return entities.size();
    }
}

