package com.javaica.avp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
@RequiredArgsConstructor
public class ByteArrayToJsonNodeConverter implements Converter<byte[], JsonNode> {

    private final ObjectMapper mapper;

    @Override
    public JsonNode convert(byte[] source) {
        String sourceString = new String(source);
        try {
            return mapper.readTree(sourceString);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot convert byte array to JsonNode", e);
        }
    }
}
