package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.entity.File;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {
    String saveFileAndGetUniqueFilename(MultipartFile file);

    byte[] getFileBytes(String imageName);

    MediaType getMediaTypeForFile(String filename);

    File uploadFile(MultipartFile file);

    Path createFilePath(String uniqueFilename);

    String generateUniqueFilename(String originalFilename);


}
