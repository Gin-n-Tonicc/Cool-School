package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.BlogController;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.services.impl.BlogServiceImpl;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@WebMvcTest(value = BlogController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = BlogController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class BlogControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BlogServiceImpl blogService;
    private List<BlogResponseDTO> blogList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        blogList = new ArrayList<>();
        blogList.add(new BlogResponseDTO());
    }

    @Test
    public void testGetAllBlogs() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetBlogById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateBlog() throws Exception {
        String validBlogDtoJson = "{ \"title\": \"Test Blog\", \"content\": \"This is a test blog.\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/blogs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBlogDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUpdateBlog() throws Exception {
        String validBlogDtoJson = "{ \"title\": \"Updated Blog\", \"content\": \"This is an updated blog.\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/blogs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBlogDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteBlogById() throws Exception {
        Long blogId = 1L;
        mockMvc.perform(delete("/api/v1/blogs/{id}", blogId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBlogsByNewest() throws Exception {
        Mockito.when(blogService.getBlogsByNewestFirst()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/sort/default"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetBlogsByNumberOfLikes() throws Exception {
        Mockito.when(blogService.getBlogsByMostLiked()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/sort/likes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetLastNRecentBlogs() throws Exception {
        int n = 5;
        Mockito.when(blogService.getLastNBlogs(n)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/blogs/mostRecent/{n}", n))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}