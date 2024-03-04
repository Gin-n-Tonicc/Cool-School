package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.BlogController;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.services.AIAssistanceService;
import com.coolSchool.coolSchool.services.BlogService;
import com.coolSchool.coolSchool.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = BlogController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = BlogController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = BlogController.class
                )
        }
)
class BlogControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BlogService blogService;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private AIAssistanceService aiAssistanceService;
    @InjectMocks
    private BlogController blogController;

    @Test
    void testGetAllBlogs() throws Exception {
        List<BlogResponseDTO> mockBlogs = new ArrayList<>();
        mockBlogs.add(new BlogResponseDTO());
        mockBlogs.add(new BlogResponseDTO());

        when(blogService.getAllBlogs(any())).thenReturn(mockBlogs);

        mockMvc.perform(get("/api/v1/blogs/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockBlogs.size()));
    }

    @Test
    void testLikeBlog() throws Exception {
        Long blogId = 1L;
        BlogResponseDTO mockBlogResponseDTO = new BlogResponseDTO();
        mockBlogResponseDTO.setId(blogId);
        mockBlogResponseDTO.setTitle("Test Blog Title");
        mockBlogResponseDTO.setContent("Test Blog Content");

        when(blogService.addLike(eq(blogId), any())).thenReturn(mockBlogResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/blogs/like/{blogId}", blogId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(blogId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockBlogResponseDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(mockBlogResponseDTO.getContent()));
    }

    @Test
    void testCreateBlog() throws Exception {
        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Test Blog Title");
        requestDTO.setContent("Test Blog Content");

        BlogResponseDTO mockResponseDTO = new BlogResponseDTO();
        mockResponseDTO.setId(1L);
        mockResponseDTO.setTitle("Test Blog Title");
        mockResponseDTO.setContent("Test Blog Content");

        when(blogService.createBlog(any(BlogRequestDTO.class), any(PublicUserDTO.class))).thenReturn(mockResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/blogs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }


    @Test
    void testUpdateBlog() throws Exception {
        Long blogId = 1L;
        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Updated Blog Title");
        requestDTO.setContent("Updated Blog Content");

        BlogResponseDTO mockResponseDTO = new BlogResponseDTO();
        mockResponseDTO.setId(blogId);
        mockResponseDTO.setTitle("Updated Blog Title");
        mockResponseDTO.setContent("Updated Blog Content");

        when(blogService.updateBlog(eq(blogId), any(BlogRequestDTO.class), any(PublicUserDTO.class))).thenReturn(mockResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/blogs/{id}", blogId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }


    @Test
    void testDeleteBlogById() throws Exception {
        Long blogId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/blogs/{id}", blogId))
                .andExpect(status().isOk())
                .andExpect(content().string("Blog with id: " + blogId + " has been deleted successfully!"));
    }
}
