package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.comment.CommentNotFoundException;
import com.coolSchool.CoolSchool.exceptions.comment.ValidationCommentException;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.models.dto.CommentDTO;
import com.coolSchool.CoolSchool.models.dto.CommentGetByBlogResponseDTO;
import com.coolSchool.CoolSchool.models.dto.CommentGetDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Blog;
import com.coolSchool.CoolSchool.models.entity.Comment;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.BlogRepository;
import com.coolSchool.CoolSchool.repositories.CommentRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.CommentService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final Validator validator;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository, BlogRepository blogRepository, Validator validator) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
        this.validator = validator;
    }

    @Override
    public List<CommentGetDTO> getAllComments() {
        List<Comment> comments = commentRepository.findByDeletedFalse();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentGetDTO.class)).toList();
    }

    @Override
    public CommentGetByBlogResponseDTO getCommentByBlogId(Long id, Integer n) {
        List<Comment> comments = commentRepository.findCommentsByBlogAndNotDeleted(id);
        int length = comments.size();

        if (n >= 1) {
            comments = comments.subList(0, Math.min(length, n));
        }

        List<CommentGetDTO> commentGetDTOs = comments.stream().map(comment -> modelMapper.map(comment, CommentGetDTO.class)).toList();

        return CommentGetByBlogResponseDTO
                .builder()
                .comments(commentGetDTOs)
                .totalComments(length)
                .build();
    }

    @Override
    public CommentGetDTO getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findByIdAndDeletedFalse(id);
        if (comment.isPresent()) {
            return modelMapper.map(comment.get(), CommentGetDTO.class);
        }
        throw new CommentNotFoundException();
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, PublicUserDTO loggedUser) {
        try {
            commentDTO.setId(null);
            commentDTO.setCreated_at(LocalDateTime.now());
            commentDTO.setOwnerId(loggedUser.getId());
            userRepository.findByIdAndDeletedFalse(commentDTO.getOwnerId()).orElseThrow(NoSuchElementException::new);
            Blog blog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(commentDTO.getBlogId()).orElseThrow(NoSuchElementException::new);
            blog.setCommentCount(blog.getCommentCount() + 1);
            blogRepository.save(blog);
            Comment commentEntity = commentRepository.save(modelMapper.map(commentDTO, Comment.class));
            return modelMapper.map(commentEntity, CommentDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCommentException(exception.getConstraintViolations());
        }
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO, PublicUserDTO loggedUser) {
        Optional<Comment> existingCommentOptional = commentRepository.findByIdAndDeletedFalse(id);
        if (existingCommentOptional.isEmpty()) {
            throw new CommentNotFoundException();
        }
        User user = userRepository.findByIdAndDeletedFalse(commentDTO.getOwnerId()).orElseThrow(NoSuchElementException::new);
        blogRepository.findByIdAndDeletedFalseIsEnabledTrue(commentDTO.getBlogId()).orElseThrow(NoSuchElementException::new);
        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), user.getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException();
        }
        Comment existingComment = existingCommentOptional.get();
        modelMapper.map(commentDTO, existingComment);

        try {
            existingComment.setId(id);
            Comment updatedComment = commentRepository.save(existingComment);
            return modelMapper.map(updatedComment, CommentDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationCommentException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public void deleteComment(Long id, PublicUserDTO loggedUser) {
        Optional<Comment> commentOptional = commentRepository.findByIdAndDeletedFalse(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (loggedUser == null || (!Objects.equals(loggedUser.getId(), comment.getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN) && !Objects.equals(loggedUser.getId(), comment.getBlogId().getOwnerId())))) {
                throw new AccessDeniedException();
            }
            Blog blog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(comment.getBlogId().getId()).orElseThrow(NoSuchElementException::new);
            blog.setCommentCount(blog.getCommentCount() - 1);
            blogRepository.save(blog);
            comment.setDeleted(true);
            commentRepository.save(comment);
        } else {
            throw new CommentNotFoundException();
        }
    }

    @Override
    public List<CommentDTO> getCommentsByNewestFirst() {
        List<Comment> comments = commentRepository.findAllByNewestFirst();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }

    @Override
    public List<CommentDTO> getCommentsByMostLiked() {
        List<Comment> comments = commentRepository.findAllByMostLiked();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }
}
