package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.models.entity.File;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;
    @Value("${upload.directory}")
    public String uploadDirectory;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFileAndGetUniqueFilename(MultipartFile file) throws IOException {
        String uniqueFilename = generateUniqueFilename(file.getOriginalFilename());
        Files.copy(file.getInputStream(), createFilePath(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    public String generateUniqueFilename(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    public Path createFilePath(String uniqueFilename) throws IOException {
        Path directoryPath = Paths.get(uploadDirectory);
        Files.createDirectories(directoryPath);
        return directoryPath.resolve(uniqueFilename);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFilename = saveFileAndGetUniqueFilename(file);
        File fileEntity = new File();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setUrl(uploadDirectory + file.getOriginalFilename());
        fileEntity.setDeleted(false);
        System.out.println(file.getContentType());
        fileEntity.setType(file.getContentType());
        fileRepository.save(fileEntity);
        return uniqueFilename;
    }

    public byte[] getFileBytes(String imageName) throws IOException {
        try {
            return Files.readAllBytes(Paths.get(uploadDirectory, imageName));
        } catch (NoSuchFileException e) {
            return new byte[0];
        }
    }

    public MediaType getMediaTypeForFile(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if ("pdf".equalsIgnoreCase(extension)) {
            return MediaType.APPLICATION_PDF;
        } else if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
            return MediaType.IMAGE_JPEG;
        } else if ("png".equalsIgnoreCase(extension)) {
            return MediaType.IMAGE_PNG;
        } else if ("xlsx".equalsIgnoreCase(extension)) {
            return MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if ("xls".equalsIgnoreCase(extension)) {
            return MediaType.valueOf("application/vnd.ms-excel");
        } else if ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension)) {
            return MediaType.valueOf("application/msword");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
