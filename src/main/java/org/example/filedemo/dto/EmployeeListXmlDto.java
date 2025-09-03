package org.example.filedemo.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "employees") // кореневий тег
public record EmployeeListXmlDto(
        @JacksonXmlElementWrapper(useWrapping = false) // щоб не було <employees><employees> два рази як загальна і додаткова обгортка
        @JacksonXmlProperty(localName = "employee")   // елемент списку
        List<EmployeeXmlDto> employees
) {}
