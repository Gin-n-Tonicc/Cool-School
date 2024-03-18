package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.coolSchool.exceptions.comment.CommentNotFoundException;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CommentRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentGetByBlogResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.coolSchool.models.entity.Blog;
import com.coolSchool.coolSchool.models.entity.Comment;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CommentRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.CommentService;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
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
    private final MessageSource messageSource;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository, BlogRepository blogRepository, MessageSource messageSource) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findByDeletedFalse();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDTO.class)).toList();
    }

    @Override
    public CommentGetByBlogResponseDTO getCommentByBlogId(Long id, Integer n) {
        List<Comment> comments = commentRepository.findCommentsByBlogAndNotDeleted(id);
        int length = comments.size();

        // Limit the number of comments if specified
        if (n >= 1) {
            comments = comments.subList(0, Math.min(length, n));
        }

        List<CommentResponseDTO> commentGetDTOs = comments.stream().map(comment -> {
            comment.setBlogId(null);  // Remove redundant blog ID from comment entity

            CommentResponseDTO commentResponseDTO = modelMapper.map(comment, CommentResponseDTO.class);
            // Map owner ID to PublicUserDTO
            commentResponseDTO.setOwnerId(modelMapper.map(comment.getOwnerId(), PublicUserDTO.class));
            return commentResponseDTO;
        }).toList();

        return CommentGetByBlogResponseDTO
                .builder()
                .comments(commentGetDTOs)
                .totalComments(length)
                .build();
    }

    @Override
    public CommentResponseDTO getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findByIdAndDeletedFalse(id);
        if (comment.isPresent()) {
            return modelMapper.map(comment.get(), CommentResponseDTO.class);
        }
        throw new CommentNotFoundException(messageSource);
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO, PublicUserDTO loggedUser) {
        Blog blog = null;

        try {
            commentDTO.setId(null);
            commentDTO.setCreated_at(LocalDateTime.now());
            commentDTO.setOwnerId(loggedUser.getId());
            userRepository.findByIdAndDeletedFalse(commentDTO.getOwnerId()).orElseThrow(() -> new UserNotFoundException(messageSource));

            // Retrieve the blog for the comment and increment the comment count
            blog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(commentDTO.getBlogId()).orElseThrow(() -> new BlogNotFoundException(messageSource));
            blog.setCommentCount(blog.getCommentCount() + 1);
            blogRepository.save(blog);
            Comment commentEntity = commentRepository.save(modelMapper.map(commentDTO, Comment.class));
            return modelMapper.map(commentEntity, CommentResponseDTO.class);
        } catch (ConstraintViolationException exception) {
            // Rollback the increment of comment count if an exception occurs during comment creation
            if (blog != null) {
                blog.setCommentCount(blog.getCommentCount() - 1);
                blogRepository.save(blog);
            }
            throw exception;
        }
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentDTO, PublicUserDTO loggedUser) {
        Optional<Comment> existingCommentOptional = commentRepository.findByIdAndDeletedFalse(id);
        if (existingCommentOptional.isEmpty()) {
            throw new CommentNotFoundException(messageSource);
        }
        User user = userRepository.findByIdAndDeletedFalse(commentDTO.getOwnerId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        blogRepository.findByIdAndDeletedFalseIsEnabledTrue(commentDTO.getBlogId()).orElseThrow(() -> new BlogNotFoundException(messageSource));
        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), user.getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            // The comment can be updated only form its owner or the ADMIN
            throw new AccessDeniedException(messageSource);
        }
        Comment existingComment = existingCommentOptional.get();
        modelMapper.map(commentDTO, existingComment);

        existingComment.setId(id);
        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, PublicUserDTO loggedUser) {
        Optional<Comment> commentOptional = commentRepository.findByIdAndDeletedFalse(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (loggedUser == null || (!Objects.equals(loggedUser.getId(), comment.getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN) && !Objects.equals(loggedUser.getId(), comment.getBlogId().getOwnerId())))) {
                // The comment can be deleted only form its owner or the ADMIN
                throw new AccessDeniedException(messageSource);
            }
            Blog blog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(comment.getBlogId().getId()).orElseThrow(() -> new BlogNotFoundException(messageSource));
            blog.setCommentCount(blog.getCommentCount() - 1);
            blogRepository.save(blog);
            comment.setDeleted(true);
            commentRepository.save(comment);
        } else {
            throw new CommentNotFoundException(messageSource);
        }
    }

    @Override
    public List<CommentResponseDTO> getCommentsByNewestFirst() {
        List<Comment> comments = commentRepository.findAllByNewestFirst();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDTO.class)).toList();
    }

    @Override
    public List<CommentResponseDTO> getCommentsByMostLiked() {
        List<Comment> comments = commentRepository.findAllByMostLiked();
        return comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDTO.class)).toList();
    }
}
