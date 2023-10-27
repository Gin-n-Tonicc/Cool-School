package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.CommentController;
import com.coolSchool.CoolSchool.models.dto.CommentDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.services.impl.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
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
    private List<CommentDTO> commentList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        commentList = new ArrayList<>();
        commentList.add(new CommentDTO());
    }

    @Test
    void testGetAllComments() throws Exception {
        when(commentService.getAllComments()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/comments/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentById() throws Exception {
        Long commentId = 1L;
        CommentDTO comment = new CommentDTO();

        when(commentService.getCommentById(commentId)).thenReturn(comment);

        mockMvc.perform(get("/api/v1/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentsByNewest() throws Exception {
        when(commentService.getCommentsByNewestFirst()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/sort/newest"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentsByNumberOfLikes() throws Exception {
        when(commentService.getCommentsByMostLiked()).thenReturn(Collections.emptyList());

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

    @Test
    @WithMockUser
    public void testCreateComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment("content");

        when(commentService.createComment(commentDTO, new PublicUserDTO())).thenReturn(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/comments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"comment\": \"content\" }")
                )
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void testUpdateComment() throws Exception {
        Long commentId = 1L;
        CommentDTO updatedCommentDTO = new CommentDTO();
        updatedCommentDTO.setComment("content");
        when(commentService.updateComment(commentId, updatedCommentDTO, new PublicUserDTO())).thenReturn(updatedCommentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"comment\": \"content\" }")
                )
                .andExpect(status().isOk());
    }
}


