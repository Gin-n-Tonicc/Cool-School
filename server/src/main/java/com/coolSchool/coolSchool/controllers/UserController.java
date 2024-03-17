package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.AdminUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller class responsible for handling user-related endpoints
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @RateLimited
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<AdminUserDTO> updateUser(@PathVariable("id") Long id, @Valid @RequestBody AdminUserDTO userDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        userService.deleteUserById(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok().build();
    }
}
