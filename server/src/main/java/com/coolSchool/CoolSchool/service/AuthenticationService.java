package com.coolSchool.CoolSchool.service;

import com.coolSchool.CoolSchool.enums.TokenType;
import com.coolSchool.CoolSchool.models.Token;
import com.coolSchool.CoolSchool.models.dto.UserDTO;
import com.coolSchool.CoolSchool.models.dto.request.RegistrationRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;


    public UserDTO register(RegistrationRequest registrationRequest){
        return userService.createUser(registrationRequest);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


//
//    public AuthenticationResponse authenticate(JWTAuthenticationRequest request) {
//        try{
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getUserName(),
//                            request.getPassword()
//                    )
//            );}
//        catch (BadCredentialsException ex){
//            throw new InvalidCredentialsException();
//        } catch (DisabledException ex){
//            throw new UserDisabledException();
//        } catch (LockedException ex){
//            throw new UserLockedException();
//        }
//
//        User user = userService.getUserByEmail(request.getUserName());
//        var jwtToken = jwtService.getGeneratedToken(user.getUsername());
//        var refreshToken = jwtService.generateRefreshToken(user.getUsername());
//        saveUserToken(user, jwtToken);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .userRole(user.getRole())
//                .build();
//    }
//    public void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException, java.io.IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsernameFromToken(refreshToken);
//        if (userEmail != null) {
//
//
//            User user = userService.getUserByEmail(userEmail);
//            MyUserDetails userDetails = new MyUserDetails(user);
//
//            if (jwtService.validateToken(refreshToken, userDetails)) {
//                String accessToken = jwtService.getGeneratedToken(user.getUsername());
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .userRole(user.getRole())
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }

}
