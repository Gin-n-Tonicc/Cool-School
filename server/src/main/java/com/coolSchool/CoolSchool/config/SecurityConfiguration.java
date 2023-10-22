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
import static com.coolSchool.CoolSchool.enums.Role.ADMIN;
import static com.coolSchool.CoolSchool.enums.Role.MANAGER;
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
                        "/api/v1/quizzes/**",
                        "/api/v1/questions/**",
                        "/api/v1/answers/**",
                        "/api/v1/userQuizzes/**",
                        "/api/v1/userAnswers/**",
                        "/api/v1/files/**",
                        "/api/v1/courses/**",
                        "/api/v1/categories/**",
                        "/api/v1/userCourses/**",
                        "/api/v1/courseSubsections/**",
                        "/api/v1/blogs/**",
                        "/api/v1/comments/**",
                        "/api/v1/resources/**",
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


                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                .requestMatchers(POST, "/api/v1/categories/**").hasAnyAuthority(ADMIN_CREATE.name())
                .requestMatchers(PUT, "/api/v1/categories/**").hasAnyAuthority(ADMIN_UPDATE.name())
                .requestMatchers(DELETE, "/api/v1/categories/**").hasAnyAuthority(ADMIN_DELETE.name())
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
