package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CommentRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentGetByBlogResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    List<CommentResponseDTO> getAllComments();

    CommentGetByBlogResponseDTO getCommentByBlogId(Long blogId, Integer n);

    CommentResponseDTO getCommentById(Long id);

    CommentResponseDTO createComment(CommentRequestDTO commentDTO, PublicUserDTO loggedUser);

    CommentResponseDTO updateComment(Long id, CommentRequestDTO commentDTO, PublicUserDTO loggedUser);

    void deleteComment(Long id, PublicUserDTO loggedUser);

    List<CommentResponseDTO> getCommentsByNewestFirst();

    List<CommentResponseDTO> getCommentsByMostLiked();
}
