package com.javaica.avp.config;

import com.javaica.avp.util.JsonNodeToByteArrayConverter;
import com.javaica.avp.util.ByteArrayToJsonNodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataJdbcConfig extends AbstractJdbcConfiguration {

    private final JsonNodeToByteArrayConverter writingConverter;
    private final ByteArrayToJsonNodeConverter readingConverter;

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(writingConverter, readingConverter));
    }
}
