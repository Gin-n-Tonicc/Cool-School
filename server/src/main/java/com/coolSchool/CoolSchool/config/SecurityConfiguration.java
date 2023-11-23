package com.coolSchool.CoolSchool.config;

import com.coolSchool.CoolSchool.exceptions.handlers.JwtAuthenticationEntryPoint;
import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.coolSchool.CoolSchool.enums.Permission.*;
import static com.coolSchool.CoolSchool.enums.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper))
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/v1/files/upload",
                        "/api/v1/files/**",
                        "/api/v1/userCourses/**",
                        "/api/v1/messages/**",
                        "/api/v1/auth/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
                )
                .permitAll()

                .requestMatchers(GET, "/api/v1/comments/**").permitAll()

                .requestMatchers(GET, "/api/v1/categories/**").permitAll()
                .requestMatchers(POST, "/api/v1/categories/**").hasAnyRole(ADMIN.name())
                .requestMatchers(PUT, "/api/v1/categories/**").hasAnyRole(ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/categories/**").hasAnyRole(ADMIN.name())

                .requestMatchers(GET, "/api/v1/reviews/**").permitAll()
                .requestMatchers(POST, "/api/v1/reviews/**").hasAnyRole(ADMIN.name(), USER.name(), TEACHER.name())

                .requestMatchers(GET, "/api/v1/courses/**").permitAll()
                .requestMatchers(POST, "/api/v1/courses/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(PUT, "/api/v1/courses/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/courses/**").hasAnyRole(TEACHER.name(), ADMIN.name())

                .requestMatchers(GET, "/api/v1/blogs/**").permitAll()
                .requestMatchers(POST, "/api/v1/blogs/**").hasAnyRole(ADMIN.name(), USER.name(), TEACHER.name())
                .requestMatchers(PUT, "/api/v1/blogs/**").hasAnyRole(ADMIN.name(), USER.name(), TEACHER.name())
                .requestMatchers(DELETE, "/api/v1/blogs/**").hasAnyRole(ADMIN.name(), USER.name(), TEACHER.name())

                .requestMatchers(GET, "/api/v1/courseSubsections/**").permitAll()
                .requestMatchers(POST, "/api/v1/courseSubsections/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(PUT, "/api/v1/courseSubsections/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/courseSubsections/**").hasAnyRole(TEACHER.name(), ADMIN.name())

                .requestMatchers(GET, "/api/v1/resources/**").permitAll()
                .requestMatchers(POST, "/api/v1/resources/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(PUT, "/api/v1/resources/**").hasAnyRole(TEACHER.name(), ADMIN.name())
                .requestMatchers(DELETE, "/api/v1/resources/**").hasAnyRole(TEACHER.name(), ADMIN.name())

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}
