package com.javaica.avp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@WritingConverter
@Component
@RequiredArgsConstructor
public class JsonNodeToPgObjectConverter implements Converter<JsonNode, PGobject> {

    private final ObjectMapper mapper;

    @Override
    public PGobject convert(JsonNode source) {
        try {
            PGobject result = new PGobject();
            result.setType("jsonb");
            result.setValue(mapper.writeValueAsString(source));
            return result;
        } catch (JsonProcessingException | SQLException e) {
            throw new IllegalArgumentException("Cannot convert JsonNode to PGobject", e);
        }
    }
}
