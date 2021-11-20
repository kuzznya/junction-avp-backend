package com.javaica.avp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("data.jdbc")
@Component
@Data
public class DataJdbcProperties {
    private JsonSupportType jsonSupport = JsonSupportType.BYTE_ARRAY;
}
