package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CategoryController;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.services.CategoryService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CategoryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CategoryController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CategoryController.class
                )
        }
)
class CategoryControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() throws Exception {
        List<CategoryDTO> mockCategories = Arrays.asList(
                new CategoryDTO(1L, "Category 1"),
                new CategoryDTO(2L, "Category 2")
        );

        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(mockCategories.size()));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Long categoryId = 1L;
        CategoryDTO mockCategory = new CategoryDTO(categoryId, "Category 1");

        when(categoryService.getCategoryById(categoryId)).thenReturn(mockCategory);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockCategory.getName()));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryDTO requestCategory = new CategoryDTO(null, "New Category");
        CategoryDTO mockCategory = new CategoryDTO(1L, "New Category");

        when(categoryService.createCategory(requestCategory)).thenReturn(mockCategory);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCategory)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockCategory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockCategory.getName()));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDTO requestCategory = new CategoryDTO(categoryId, "Updated Category");

        when(categoryService.updateCategory(categoryId, requestCategory)).thenReturn(requestCategory);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCategory)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(requestCategory.getName()));
    }

    @Test
    void testDeleteCategoryById() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/{id}", categoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Category with id: " + categoryId + " has been deleted successfully!"));

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}
