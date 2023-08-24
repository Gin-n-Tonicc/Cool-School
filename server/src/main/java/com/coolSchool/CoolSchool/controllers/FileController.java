package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.services.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
        byte[] imageBytes = fileService.getImageBytes(filename);
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if ("pdf".equalsIgnoreCase(extension)) {
            mediaType = MediaType.APPLICATION_PDF;
        } else if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if ("png".equalsIgnoreCase(extension)) {
            mediaType = MediaType.IMAGE_PNG;
        } else if ("xlsx".equalsIgnoreCase(extension)) {
            mediaType = MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if ("xls".equalsIgnoreCase(extension)) {
            mediaType = MediaType.valueOf("application/vnd.ms-excel");
        } else if ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension)) {
            mediaType = MediaType.valueOf("application/msword");
        }
        headers.setContentType(mediaType);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}

