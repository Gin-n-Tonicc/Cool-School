package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CommentController;
import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.coolSchool.services.impl.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@WebMvcTest(value = CommentController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CommentController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class CommentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommentServiceImpl commentService;
    private List<CommentResponseDTO> commentList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        commentList = new ArrayList<>();
        commentList.add(new CommentResponseDTO());
    }

    @Test
    void testGetAllComments() throws Exception {
        Mockito.when(commentService.getAllComments()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/comments/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentById() throws Exception {
        Long commentId = 1L;
        CommentResponseDTO comment = new CommentResponseDTO();

        Mockito.when(commentService.getCommentById(commentId)).thenReturn(comment);

        mockMvc.perform(get("/api/v1/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentsByNewest() throws Exception {
        Mockito.when(commentService.getCommentsByNewestFirst()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/sort/newest"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentsByNumberOfLikes() throws Exception {
        Mockito.when(commentService.getCommentsByMostLiked()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/sort/default"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCommentById() throws Exception {
        Long commentId = 1L;
        mockMvc.perform(delete("/api/v1/comments/{id}", commentId))
                .andExpect(status().isOk());
    }
}


