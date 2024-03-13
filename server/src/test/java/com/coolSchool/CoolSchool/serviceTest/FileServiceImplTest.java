package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.exceptions.common.InternalServerErrorException;
import com.coolSchool.coolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.coolSchool.exceptions.files.UnsupportedFileTypeException;
import com.coolSchool.coolSchool.models.entity.File;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.services.impl.FileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String imageDirectory = "server/src/main/resources/static/uploads/";
        fileService = new FileServiceImpl(fileRepository, messageSource, imageDirectory);
    }

    @Test
    void testGetMediaTypeForPdf() {
        MediaType result = fileService.getMediaTypeForFile("file.pdf");
        assertEquals(MediaType.APPLICATION_PDF, result);
    }

    @Test
    void testGetMediaTypeForJpg() {
        MediaType result = fileService.getMediaTypeForFile("file.jpg");
        assertEquals(MediaType.IMAGE_JPEG, result);
    }

    @Test
    void testGetMediaTypeForPng() {
        MediaType result = fileService.getMediaTypeForFile("file.png");
        assertEquals(MediaType.IMAGE_PNG, result);
    }

    @Test
    void testGetMediaTypeForXlsx() {
        MediaType result = fileService.getMediaTypeForFile("file.xlsx");
        assertEquals(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), result);
    }

    @Test
    void testGetMediaTypeForXls() {
        MediaType result = fileService.getMediaTypeForFile("file.xls");
        assertEquals(MediaType.valueOf("application/vnd.ms-excel"), result);
    }

    @Test
    void testGetMediaTypeForDoc() {
        MediaType result = fileService.getMediaTypeForFile("file.doc");
        assertEquals(MediaType.valueOf("application/msword"), result);
    }

    @Test
    void testGetMediaTypeForDocx() {
        MediaType result = fileService.getMediaTypeForFile("file.docx");
        assertEquals(MediaType.valueOf("application/msword"), result);
    }

    @Test
    void testGetMediaTypeForUnknownExtension() {
        MediaType result = fileService.getMediaTypeForFile("file.unknown");
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result);
    }

    @Test
    void testGenerateUniqueFilename() {
        String originalFilename = "example.jpg";
        String uniqueFilename = fileService.generateUniqueFilename(originalFilename);
        Assertions.assertTrue(uniqueFilename.matches("^\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}_" + originalFilename + "$"));
    }

    @Test
    void testGetFileBytes() throws IOException {
        String imageName = "test-image.jpg";
        String imageDirectory = "server/src/main/resources/static/uploads/";
        Path imagePath = Path.of(imageDirectory, imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, "Test image content".getBytes());

        byte[] imageBytes = fileService.getFileBytes(imageName);

        Assertions.assertArrayEquals("Test image content".getBytes(), imageBytes);
    }

    @Test
    void testGetImageBytesNonExistentImage() throws IOException {
        String nonExistentImageName = "non-existent-image.jpg";
        org.testng.Assert.assertThrows(FileNotFoundException.class, () -> fileService.getFileBytes(nonExistentImageName));
    }

    @Test
    void testUploadFileThrowUnsupportedFileTypeException() {
        MockMultipartFile file = new MockMultipartFile(
                "test-file.exe",
                "test-file.exe",
                "text/plain",
                "Test file content".getBytes()
        );
        org.testng.Assert.assertThrows(UnsupportedFileTypeException.class, () -> fileService.uploadFile(file));
    }

    @Test
    void testUploadFile() {
        MockMultipartFile file = new MockMultipartFile(
                "test-file.pdf",
                "test-file.pdf",
                "text/plain",
                "Test file content".getBytes()
        );
        String uniqueFilename = String.valueOf(fileService.uploadFile(file));
        Assertions.assertNotNull(uniqueFilename);
        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test
    void testUploadFileThrowNullPointerException() {
        MockMultipartFile file = null;
        assertThrows(NullPointerException.class, () -> {
            fileService.uploadFile(file);
        });
        verify(fileRepository, never()).save(any(File.class));
    }

    @Test
    void testSaveFileAndGetUniqueFilename() {
        MockMultipartFile file = new MockMultipartFile(
                "test-file.txt",
                "test-file.txt",
                "text/plain",
                "Test file content".getBytes()
        );
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("temp-dir");
        } catch (IOException e) {
            fail("Failed to create temporary directory");
            return;
        }
        String uniqueFilename = null;
        try {
            uniqueFilename = fileService.saveFileAndGetUniqueFilename(file);
        } catch (InternalServerErrorException e) {
            fail("Unexpected InternalServerErrorException");
        }
        assertNotNull(uniqueFilename);
    }

    @Test
    void testSaveFileAndGetUniqueFilenameThrowException() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "test-file.txt",
                "test-file.txt",
                "text/plain",
                "Test file content".getBytes()
        );

        // Create a temporary directory for testing
        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("temp-dir");
        } catch (IOException e) {
            fail("Failed to create temporary directory");
            return;
        }
        MultipartFile mockedFile = mock(MultipartFile.class);
        when(mockedFile.getInputStream()).thenThrow(IOException.class);
        assertThrows(InternalServerErrorException.class, () -> {
            fileService.saveFileAndGetUniqueFilename(mockedFile);
        });
        try {
            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            fail("Failed to delete temporary directory");
        }
    }
}
