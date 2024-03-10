package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.coolSchool.models.entity.Comment;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CommentRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        modelMapper = new ModelMapper();
        userRepository = mock(UserRepository.class);
        blogRepository = mock(BlogRepository.class);

        commentService = new CommentServiceImpl(commentRepository, modelMapper, userRepository, blogRepository, messageSource);
    }

    @Test
    void testGetAllComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        comments.add(new Comment());
        when(commentRepository.findByDeletedFalse()).thenReturn(comments);
        List<CommentResponseDTO> commentDTOs = commentService.getAllComments();
        Assertions.assertNotNull(commentDTOs);
        Assertions.assertEquals(2, commentDTOs.size());
    }

    @Test
    void testGetCommentById() {
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(comment));
        CommentResponseDTO commentDTO = commentService.getCommentById(1L);
        Assertions.assertNotNull(commentDTO);

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

}
