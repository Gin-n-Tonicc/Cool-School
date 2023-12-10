package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.ResourceController;
import com.coolSchool.coolSchool.models.dto.common.ResourceDTO;
import com.coolSchool.coolSchool.services.impl.ResourceServiceImpl;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ResourceController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = ResourceController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class ResourceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ResourceServiceImpl resourceService;
    private List<ResourceDTO> resourceList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        resourceList = new ArrayList<>();
        resourceList.add(new ResourceDTO());
    }

    @Test
    void testGetAllResources() throws Exception {
        Mockito.when(resourceService.getAllResources()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/resources/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteResourceById() throws Exception {
        Long resourceId = 1L;
        mockMvc.perform(delete("/api/v1/resources/{id}", resourceId))
                .andExpect(status().isOk());
    }
}

