package com.coolSchool.coolSchool.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

// Configuration class for web-related configurations
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final FrontendConfig frontendConfig;

    // Method to configure CORS mappings
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // Allow CORS for all endpoints
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowedOrigins(frontendConfig.getBaseUrl()) // Allowed origins (frontend URL)
                .allowCredentials(true); // Allow credentials (cookies, authorization headers)
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}
