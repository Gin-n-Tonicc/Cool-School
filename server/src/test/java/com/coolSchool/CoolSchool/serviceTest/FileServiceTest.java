package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.repositories.FileRepository;
import com.coolSchool.CoolSchool.services.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileServiceImpl fileService;
    @Mock
    private FileRepository fileRepository;

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
        String originalFilename = "myfile.txt";
        String uniqueFilename = fileService.generateUniqueFilename(originalFilename);

        assertTrue(uniqueFilename.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}_" + originalFilename));
    }
}
