package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CategoryController;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.services.impl.CategoryServiceImpl;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CategoryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CategoryController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class CategoriesControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryServiceImpl categoryService;
    private List<CategoryDTO> categoryList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryDTO());
    }

    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/categories/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Long categoryId = 1L;
        CategoryDTO category = new CategoryDTO();

        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDTO category = new CategoryDTO();
        String categoryJson = objectMapper.writeValueAsString(category);

        Mockito.when(categoryService.createCategory(Mockito.any(CategoryDTO.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDTO updatedCategory = new CategoryDTO();
        String updatedCategoryJson = objectMapper.writeValueAsString(updatedCategory);

        Mockito.when(categoryService.updateCategory(Mockito.eq(categoryId), Mockito.any(CategoryDTO.class)))
                .thenReturn(updatedCategory);

        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCategoryJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCategoryById() throws Exception {
        Long categoryId = 1L;
        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isOk());
    }
}
