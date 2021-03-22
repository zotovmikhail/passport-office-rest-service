package com.zotov.edu.passportofficerestservice.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageSerializer<T> extends JsonSerializer<Page<T>> {

    @Override
    public void serialize(Page<T> page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeNumberField("size",page.getSize());
        jsonGenerator.writeNumberField("totalPages",page.getTotalPages());
        jsonGenerator.writeObjectField("content",page.getContent());
        jsonGenerator.writeNumberField("totalElements",page.getTotalElements());

        jsonGenerator.writeEndObject();
    }
}
