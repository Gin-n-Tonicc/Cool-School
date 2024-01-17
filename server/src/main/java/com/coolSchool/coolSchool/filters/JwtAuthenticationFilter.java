package com.coolSchool.coolSchool.filters;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.repositories.TokenRepository;
import com.coolSchool.coolSchool.services.JwtService;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.utils.CookieHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.coolSchool.coolSchool.services.impl.security.TokenServiceImpl.AUTH_COOKIE_KEY_JWT;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String userKey = "user";
    private final JwtService jwtService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.contains("/api/v1/auth") && !path.contains("/api/v1/auth/complete-oauth")) {
            filterChain.doFilter(request, response);
            return;
        }

        request.setAttribute(userKey, null);

        final String jwt = CookieHelper.readCookie(AUTH_COOKIE_KEY_JWT, request.getCookies()).orElse(null);

        if (jwt == null || jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.findByEmail(userEmail);

            boolean isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            request.setAttribute(userKey, modelMapper.map(userDetails, PublicUserDTO.class));
        }

        filterChain.doFilter(request, response);
    }
}
