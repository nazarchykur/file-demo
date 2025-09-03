package org.example.filedemo.export.localized.service;

import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

public abstract class AbstractExportTranslationService {

    protected final MessageSource messageSource;

    protected AbstractExportTranslationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String[] getHeaders(Locale locale) {
        return getHeaderFieldKeys().stream()
                .map(field -> getHeaderPrefix() + field)
                .map(key -> messageSource.getMessage(key, null, locale))
                .toArray(String[]::new);
    }

    protected abstract List<String> getHeaderFieldKeys();

    protected abstract String getHeaderPrefix();
}

