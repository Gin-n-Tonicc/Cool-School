package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CommentController;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CommentRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentGetByBlogResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.CommentResponseDTO;
import com.coolSchool.coolSchool.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CommentController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CommentController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CommentController.class
                )
        }
)
class CommentControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;

    @Test
    void testGetAllComments() throws Exception {
        List<CommentResponseDTO> mockComments = Arrays.asList(
                new CommentResponseDTO(),
                new CommentResponseDTO()
        );
        mockComments.get(0).setId(1L);
        mockComments.get(0).setComment("Comment 1");
        mockComments.get(1).setId(2L);
        mockComments.get(1).setComment("Comment 2");

        when(commentService.getAllComments()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockComments.size()));
    }

    @Test
    void testGetCommentsByBlog() throws Exception {
        Long blogId = 1L;
        List<CommentResponseDTO> mockComments = Arrays.asList(
                new CommentResponseDTO(),
                new CommentResponseDTO()
        );
        mockComments.get(0).setId(1L);
        mockComments.get(0).setComment("Comment 1");
        mockComments.get(1).setId(2L);
        mockComments.get(1).setComment("Comment 2");


        when(commentService.getCommentByBlogId(blogId, -1)).thenReturn(new CommentGetByBlogResponseDTO());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/blog/{blogId}", blogId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCommentById() throws Exception {
        Long commentId = 1L;
        CommentResponseDTO mockComment = new CommentResponseDTO();
        mockComment.setId(commentId);
        mockComment.setComment("Test Comment");

        when(commentService.getCommentById(commentId)).thenReturn(mockComment);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/{id}", commentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(commentId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(mockComment.getComment()));
    }

    @Test
    void testCreateComment() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setComment("New Comment");

        CommentResponseDTO mockResponseDTO = new CommentResponseDTO();
        mockResponseDTO.setId(1L);
        mockResponseDTO.setComment("New Comment");


        when(commentService.createComment(eq(requestDTO), any(PublicUserDTO.class)))
                .thenReturn(mockResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/comments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void testUpdateComment() throws Exception {
        Long commentId = 1L;
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setComment("Updated Comment");

        CommentResponseDTO mockResponseDTO = new CommentResponseDTO();
        mockResponseDTO.setId(commentId);
        mockResponseDTO.setComment("Updated Comment");

        when(commentService.updateComment(eq(commentId), eq(requestDTO), any(PublicUserDTO.class)))
                .thenReturn(mockResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteCommentById() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/{id}", commentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Comment with id: " + commentId + " has been deleted successfully!"));
    }

    @Test
    void testGetCommentsByNewest() throws Exception {
        List<CommentResponseDTO> mockComments = Arrays.asList(
                new CommentResponseDTO(),
                new CommentResponseDTO()
        );
        mockComments.get(0).setId(1L);
        mockComments.get(0).setComment("Comment 1");
        mockComments.get(1).setId(2L);
        mockComments.get(1).setComment("Comment 2");

        when(commentService.getCommentsByNewestFirst()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/sort/newest"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockComments.size()));
    }

    @Test
    void testGetCommentsByNumberOfLikes() throws Exception {
        List<CommentResponseDTO> mockComments = Arrays.asList(
                new CommentResponseDTO(),
                new CommentResponseDTO()
        );
        mockComments.get(0).setId(1L);
        mockComments.get(0).setComment("Comment 1");
        mockComments.get(1).setId(2L);
        mockComments.get(1).setComment("Comment 2");

        when(commentService.getCommentsByMostLiked()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/sort/default"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockComments.size()));
    }
}

