package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.FileController;
import com.coolSchool.coolSchool.services.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileControllerIntegrationTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetFile() throws IOException {
        byte[] fileBytes = "Hello, World!".getBytes();
        MediaType mediaType = MediaType.TEXT_PLAIN;

        when(fileService.getFileBytes("test.txt")).thenReturn(fileBytes);
        when(fileService.getMediaTypeForFile("test.txt")).thenReturn(mediaType);

        ResponseEntity<byte[]> response = fileController.getFile("test.txt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mediaType, response.getHeaders().getContentType());
        assertEquals(fileBytes.length, Objects.requireNonNull(response.getBody()).length);
        assertEquals("Hello, World!", new String(response.getBody()));
    }
}

