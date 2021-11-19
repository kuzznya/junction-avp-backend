package com.javaica.avp.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("security.admin")
@Component
@Data
@Validated
public class AdminProperties {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
