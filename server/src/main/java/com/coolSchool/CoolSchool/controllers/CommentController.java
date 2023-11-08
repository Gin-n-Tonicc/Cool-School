package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.request.CommentRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.CoolSchool.services.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentResponseDTO> createComment(@Valid @RequestBody CommentRequestDTO commentDTO, HttpServletRequest httpServletRequest) {
        CommentResponseDTO cratedComment = commentService.createComment(commentDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentRequestDTO commentDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        commentService.deleteComment(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Comment with id: " + id + " has been deleted successfully!");
    }

    @GetMapping("/sort/newest")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByNewest() {
        return ResponseEntity.ok(commentService.getCommentsByNewestFirst());
    }

    @GetMapping("/sort/default")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByNumberOfLikes() {
        return ResponseEntity.ok(commentService.getCommentsByMostLiked());
    }
}

