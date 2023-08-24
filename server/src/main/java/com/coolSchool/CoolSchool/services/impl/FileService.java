package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.FileType;
import com.coolSchool.CoolSchool.models.entity.File;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.directory}")
    private String uploadDirectory;
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFileAndGetUniqueFilename(MultipartFile file) throws IOException {
        String uniqueFilename = generateUniqueFilename(file.getOriginalFilename());
        Files.copy(file.getInputStream(), createFilePath(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    String generateUniqueFilename(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

    Path createFilePath(String uniqueFilename) throws IOException {
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
        fileEntity.setType(FileType.JPG);
        fileRepository.save(fileEntity);
        return uniqueFilename;
    }
    public byte[] getImageBytes(String imageName) throws IOException {
        try {
            return Files.readAllBytes(Paths.get(uploadDirectory, imageName));
        } catch (NoSuchFileException e) {
            return new byte[0];
        }
    }
}
