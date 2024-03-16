package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.comment.CommentNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.CommentDTO;
import com.coolSchool.coolSchool.models.dto.request.CommentRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentGetByBlogResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.coolSchool.models.entity.Blog;
import com.coolSchool.coolSchool.models.entity.Comment;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CommentRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.CommentServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    PublicUserDTO publicUserDTO;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private ModelMapper modelMapper;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        publicUserDTO = new PublicUserDTO(1L, "user", "user", "user@gmail.com", Role.USER, "description", false);
        modelMapper = new ModelMapper();
        commentService = new CommentServiceImpl(commentRepository, modelMapper, userRepository, blogRepository, messageSource);
    }

    @Test
    public void testDeleteComment_CommentPresent() {
        Long commentId = 1L;

        User user = new User();
        user.setId(1L);
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setCommentCount(0);

        Comment comment = new Comment();
        comment.setBlogId(blog);
        comment.setDeleted(false);
        comment.setOwnerId(user);

        Optional<Comment> commentOptional = Optional.of(comment);

        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(commentOptional);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));

        assertDoesNotThrow(() -> commentService.deleteComment(commentId, publicUserDTO));
        assertTrue(comment.isDeleted());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetAllComments() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment());
        Mockito.when(commentRepository.findByDeletedFalse()).thenReturn(commentList);
        List<CommentResponseDTO> result = commentService.getAllComments();
        assertNotNull(result);
        assertEquals(commentList.size(), result.size());
    }

    @Test
    void testGetCommentById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        Optional<Comment> commentOptional = Optional.of(comment);
        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(commentOptional);
        CommentDTO result = commentService.getCommentById(commentId);
        assertNotNull(result);
    }

    @Test
    void testGetCommentByIdNotFound() {
        Long commentId = 1L;
        Optional<Comment> commentOptional = Optional.empty();
        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(commentOptional);
        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(commentId));
    }

    @Test
    void testCreateComment() {
        CommentRequestDTO commentDTO = new CommentRequestDTO();
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        User user = new User();
        user.setId(1L);
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setCommentCount(0);

        comment.setBlogId(blog);
        comment.setDeleted(false);
        comment.setOwnerId(user);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO result = commentService.createComment(commentDTO, publicUserDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        CommentRequestDTO updatedCommentDTO = new CommentRequestDTO();
        Comment existingComment = new Comment();
        Optional<Comment> existingCommentOptional = Optional.of(existingComment);

        User user = new User();
        user.setId(1L);
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setCommentCount(0);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));
        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(existingCommentOptional);
        when(commentRepository.save(any(Comment.class))).thenReturn(existingComment);

        CommentDTO result = commentService.updateComment(commentId, updatedCommentDTO, publicUserDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCommentNotFound() {
        Long nonExistentCommentId = 99L;
        CommentRequestDTO updatedCommentDTO = new CommentRequestDTO();
        when(commentRepository.findByIdAndDeletedFalse(nonExistentCommentId)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(nonExistentCommentId, updatedCommentDTO, new PublicUserDTO()));
    }

    @Test
    void testDeleteCommentNotFound() {
        Long nonExistentCommentId = 99L;

        when(commentRepository.findByIdAndDeletedFalse(nonExistentCommentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(nonExistentCommentId, new PublicUserDTO()));
    }

    @Test
    void testCreateComment_ValidationException() {
        CommentRequestDTO commentDTO = new CommentRequestDTO();
        commentDTO.setOwnerId(null);
        commentDTO.setComment(null);

        User user = new User();
        user.setId(1L);
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setCommentCount(0);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));
        when(commentRepository.save(any(Comment.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> commentService.createComment(commentDTO, publicUserDTO));
    }

    @Test
    void testUpdateComment_ValidationException() {
        Long commentId = 1L;
        CommentRequestDTO commentDTO = new CommentRequestDTO();
        commentDTO.setComment(null);
        Comment existingComment = new Comment();
        Optional<Comment> existingCommentOptional = Optional.of(existingComment);

        User user = new User();
        user.setId(2L);

        Blog blog = new Blog();
        blog.setId(2L);
        blog.setCommentCount(0);

        publicUserDTO.setRole(Role.ADMIN);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));
        when(commentRepository.findByIdAndDeletedFalse(commentId)).thenReturn(existingCommentOptional);
        when(commentRepository.save(any(Comment.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> commentService.updateComment(commentId, commentDTO, publicUserDTO));
    }

    @Test
    void testCreateComment_DataIntegrityViolationException() {
        CommentRequestDTO commentDTO = new CommentRequestDTO();
        commentDTO.setId(1L);
        commentDTO.setComment("Test Comment");

        User user = new User();
        user.setId(2L);

        Blog blog = new Blog();
        blog.setId(2L);
        blog.setCommentCount(0);

        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(user));
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(any())).thenReturn(Optional.of(blog));
        when(commentRepository.save(any(Comment.class))).thenThrow(CommentNotFoundException.class);

        assertThrows(CommentNotFoundException.class, () -> commentService.createComment(commentDTO, publicUserDTO));
    }

    @Test
    void testGetCommentsByNewestFirst() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        when(commentRepository.findAllByNewestFirst()).thenReturn(comments);
        List<CommentResponseDTO> commentDTOs = commentService.getCommentsByNewestFirst();
        Assertions.assertNotNull(commentDTOs);
        Assertions.assertEquals(2, commentDTOs.size());
    }

    @Test
    void testGetCommentByBlogId() {
        Long blogId = 1L;
        int n = 5;
        List<Comment> comments = new ArrayList<>();

        when(commentRepository.findCommentsByBlogAndNotDeleted(anyLong())).thenReturn(comments);

        CommentGetByBlogResponseDTO responseDTO = commentService.getCommentByBlogId(blogId, n);

        assertNotNull(responseDTO);
        assertEquals(comments.size(), responseDTO.getTotalComments());
        assertTrue(responseDTO.getComments().size() <= n);
    }

    @Test
    void testGetCommentsByMostLiked() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAllByMostLiked()).thenReturn(comments);

        List<CommentResponseDTO> responseDTOs = commentService.getCommentsByMostLiked();

        assertNotNull(responseDTOs);
    }
}
