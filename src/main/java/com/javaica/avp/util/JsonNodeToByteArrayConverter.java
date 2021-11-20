package com.javaica.avp.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
@RequiredArgsConstructor
public class JsonNodeToByteArrayConverter implements Converter<JsonNode, byte[]> {

    private final ObjectMapper mapper;

    @Override
    public byte[] convert(JsonNode source) {
        try {
            return mapper.writeValueAsBytes(source);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert JsonNode to byte array");
        }
    }
}
