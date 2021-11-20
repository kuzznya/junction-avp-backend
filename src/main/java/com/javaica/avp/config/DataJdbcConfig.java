package com.javaica.avp.config;

import com.javaica.avp.config.props.DataJdbcProperties;
import com.javaica.avp.util.JsonNodeToByteArrayConverter;
import com.javaica.avp.util.ByteArrayToJsonNodeConverter;
import com.javaica.avp.util.JsonNodeToPgObjectConverter;
import com.javaica.avp.util.PgObjectToJsonNodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataJdbcConfig extends AbstractJdbcConfiguration {

    private final JsonNodeToByteArrayConverter byteArrayWritingConverter;
    private final ByteArrayToJsonNodeConverter byteArrayReadingConverter;
    private final JsonNodeToPgObjectConverter pgObjectWritingConverter;
    private final PgObjectToJsonNodeConverter pgObjectReadingConverter;
    private final DataJdbcProperties dataJdbcProperties;

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        switch (dataJdbcProperties.getJsonSupport()) {
            case POSTGRES:
                return new JdbcCustomConversions(List.of(pgObjectWritingConverter, pgObjectReadingConverter));
            case BYTE_ARRAY:
            default:
                return new JdbcCustomConversions(List.of(byteArrayWritingConverter, byteArrayReadingConverter));
        }
    }
}
