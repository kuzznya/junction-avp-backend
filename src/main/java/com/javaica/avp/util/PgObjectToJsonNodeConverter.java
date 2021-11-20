package com.javaica.avp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
@RequiredArgsConstructor
public class PgObjectToJsonNodeConverter implements Converter<PGobject, JsonNode> {

    private final ObjectMapper mapper;

    @Override
    public JsonNode convert(PGobject source) {
        try {
            return mapper.readTree(source.getValue());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot convert PGobject to JsonNode", e);
        }
    }
}
