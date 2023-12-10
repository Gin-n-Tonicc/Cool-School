package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.FileController;
import com.coolSchool.coolSchool.services.impl.FileServiceImpl;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@WebMvcTest(value = FileController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = FileController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
public class FileControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileServiceImpl fileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFile() throws Exception {
        byte[] fileContent = "Test file content".getBytes();
        String filename = "test-file.txt";
        MockMultipartFile file = new MockMultipartFile("file", filename, MediaType.TEXT_PLAIN_VALUE, fileContent);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
