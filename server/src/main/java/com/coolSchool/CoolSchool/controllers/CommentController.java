package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.CoolSchool.models.dto.CommentDTO;
import com.coolSchool.CoolSchool.models.dto.CommentGetDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
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
    public ResponseEntity<List<CommentGetDTO>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentGetDTO>> getCommentsByBlog(@PathVariable(name = "blogId") Long id) {
        return ResponseEntity.ok(commentService.getCommentByBlogId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentGetDTO> getCommentById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO, HttpServletRequest httpServletRequest) {
        CommentDTO cratedComment = commentService.createComment(commentDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentDTO commentDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        commentService.deleteComment(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Comment with id: " + id + " has been deleted successfully!");
    }

    @GetMapping("/sort/newest")
    public ResponseEntity<List<CommentDTO>> getCommentsByNewest() {
        return ResponseEntity.ok(commentService.getCommentsByNewestFirst());
    }

    @GetMapping("/sort/default")
    public ResponseEntity<List<CommentDTO>> getCommentsByNumberOfLikes() {
        return ResponseEntity.ok(commentService.getCommentsByMostLiked());
    }
}

