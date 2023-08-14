package com.coolSchool.CoolSchool.controller;

import com.coolSchool.CoolSchool.models.dto.UserDTO;
import com.coolSchool.CoolSchool.models.dto.request.RegistrationRequest;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.service.AuthenticationService;
import com.coolSchool.CoolSchool.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher publisher;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Validated @RequestBody RegistrationRequest request, @RequestParam("appUrl") String appUrl) {
        UserDTO user = authenticationService.register(request);
        return new ResponseEntity<>("Успешна регистрация.", HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

