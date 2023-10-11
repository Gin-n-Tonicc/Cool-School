package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.UserCourseDTO;
import com.coolSchool.CoolSchool.services.UserCourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userCourses")
public class UserCourseController {
    private final UserCourseService userCourseService;

    public UserCourseController(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserCourseDTO>> getAllUserCourses() {
        return ResponseEntity.ok(userCourseService.getAllUserCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCourseDTO> getUserCourseById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userCourseService.getUserCourseById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserCourseDTO> createUserCourse(@Valid @RequestBody UserCourseDTO userCourseDTO) {
        UserCourseDTO cratedUserCourse = userCourseService.createUserCourse(userCourseDTO);
        return new ResponseEntity<>(cratedUserCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCourseDTO> updateUserCourse(@PathVariable("id") Long id, @Valid @RequestBody UserCourseDTO userCourseDTO) {
        return ResponseEntity.ok(userCourseService.updateUserCourse(id, userCourseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserCourseById(@PathVariable("id") Long id) {
        userCourseService.deleteUserCourse(id);
        return ResponseEntity.ok("UserCourse with id: " + id + " has been deleted successfully!");
    }
}
