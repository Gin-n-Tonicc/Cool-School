package com.coolSchool.coolSchool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server.frontend")
public class FrontendConfig {
    private String baseUrl;
    private String loginUrl;
    private String finishRegisterUrl;
}