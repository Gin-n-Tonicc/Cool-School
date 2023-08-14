package com.coolSchool.CoolSchool.filters;

import com.coolSchool.CoolSchool.models.Token;
import com.coolSchool.CoolSchool.repository.TokenRepository;
import com.coolSchool.CoolSchool.security.MyUserDetails;
import com.coolSchool.CoolSchool.service.JWTService;
import com.coolSchool.CoolSchool.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final MyUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = null;
        String username = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsernameFromToken(token);
            } catch (ExpiredJwtException ex) {
                Optional<Token> tokenDb = tokenRepository.findByTokenValue(token);
                if (tokenDb.isPresent()) {
                    tokenDb.get().setRevoked(true);
                    tokenDb.get().setExpired(true);
                    tokenRepository.save(tokenDb.get());
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
            boolean isTokenValid = tokenRepository.findByTokenValue(token)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.validateToken(token, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info("JWT filter is successful for user with email : " + username);
            }
        }
        filterChain.doFilter(request, response);
    }
}
