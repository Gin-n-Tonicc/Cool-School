package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.CommentDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(Long id);

    CommentDTO createComment(CommentDTO commentDTO, PublicUserDTO loggedUser);

    CommentDTO updateComment(Long id, CommentDTO commentDTO, PublicUserDTO loggedUser);

    void deleteComment(Long id, PublicUserDTO loggedUser);

    List<CommentDTO> getCommentsByNewestFirst();

    List<CommentDTO> getCommentsByMostLiked();
}
