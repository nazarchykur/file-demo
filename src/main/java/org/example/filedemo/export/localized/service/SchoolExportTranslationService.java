package org.example.filedemo.export.localized.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchoolExportTranslationService extends AbstractExportTranslationService {

    private static final String HEADER_PREFIX = "export.school.header.";

    public SchoolExportTranslationService(MessageSource messageSource) {
        super(messageSource);
    }

    @Override
    protected String getHeaderPrefix() {
        return HEADER_PREFIX;
    }

    @Override
    protected List<String> getHeaderFieldKeys() {
        return List.of("id", "name", "city", "studentsCount");
    }
}

