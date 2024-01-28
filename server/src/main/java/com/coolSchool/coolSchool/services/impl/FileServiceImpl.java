package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.enums.FileType;
import com.coolSchool.coolSchool.exceptions.common.InternalServerErrorException;
import com.coolSchool.coolSchool.exceptions.files.DirectoryCreationException;
import com.coolSchool.coolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.coolSchool.exceptions.files.UnsupportedFileTypeException;
import com.coolSchool.coolSchool.models.entity.File;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import static org.codehaus.plexus.util.FileUtils.getExtension;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final MessageSource messageSource;
    @Value("${upload.directory}")
    public String uploadDirectory;

    public FileServiceImpl(FileRepository fileRepository, MessageSource messageSource, @Value("${upload.directory}") String uploadDirectory) {
        this.fileRepository = fileRepository;
        this.messageSource = messageSource;
        this.uploadDirectory = uploadDirectory;
    }

    @Override
    public String generateUniqueFilename(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    @Override
    public Path createFilePath(String uniqueFilename) {
        Path directoryPath = Paths.get(uploadDirectory);
        try {
            Files.createDirectories(directoryPath);
            return directoryPath.resolve(uniqueFilename);
        } catch (IOException e) {
            throw new DirectoryCreationException(messageSource);
        }
    }

    @Override
    public File uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String extension = getExtension(originalFilename);

            if (!FileType.isSupportedExtension(extension)) {
                throw new UnsupportedFileTypeException(messageSource);
            }

            String uniqueFilename = saveFileAndGetUniqueFilename(file);
            File fileEntity = new File();
            fileEntity.setName(originalFilename);
            fileEntity.setUrl(uploadDirectory + uniqueFilename);
            fileEntity.setDeleted(false);
            fileEntity.setType(file.getContentType());

            return fileRepository.save(fileEntity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid argument.", e);
        }
    }

    @Override
    public byte[] getFileBytes(String imageName) {
        try {
            return Files.readAllBytes(Paths.get(uploadDirectory, imageName));
        } catch (NoSuchFileException e) {
            throw new FileNotFoundException(messageSource);
        } catch (IOException e) {
            throw new InternalServerErrorException(messageSource);
        }
    }

    @Override
    public String saveFileAndGetUniqueFilename(MultipartFile file) {
        try {
            String uniqueFilename = generateUniqueFilename(file.getOriginalFilename());
            Files.copy(file.getInputStream(), createFilePath(uniqueFilename), StandardCopyOption.REPLACE_EXISTING);
            return uniqueFilename;
        } catch (IOException e) {
            throw new InternalServerErrorException(messageSource);
        }
    }

    @Override
    public MediaType getMediaTypeForFile(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return FileType.getMediaTypeForExtension(extension);
    }

}
