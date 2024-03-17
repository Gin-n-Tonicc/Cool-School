package com.coolSchool.coolSchool.enums;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration representing supported file types along with their corresponding extensions and media types.
 * This class is necessary to provide a centralized way to manage and identify file types in the application.
 * It helps in mapping file extensions to their corresponding media types, which is useful for handling file uploads,
 * downloads, and processing based on their types.
 */
public enum FileType {
    PDF("pdf", MediaType.APPLICATION_PDF),
    JPEG("jpg", MediaType.IMAGE_JPEG),
    JPG("jpeg", MediaType.IMAGE_JPEG),
    PNG("png", MediaType.IMAGE_PNG),
    XLSX("xlsx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
    XLS("xls", MediaType.valueOf("application/vnd.ms-excel")),
    DOC("doc", MediaType.valueOf("application/msword")),
    PPTX("pptx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.presentationml.presentation")),
    DOCX("docx", MediaType.valueOf("application/msword"));

    private static final Map<String, MediaType> FILE_TYPE_MEDIA_TYPE_MAP = new HashMap<>();

    static {
        for (FileType fileType : values()) {
            FILE_TYPE_MEDIA_TYPE_MAP.put(fileType.extension, fileType.mediaType);
        }
    }

    private final String extension;
    private final MediaType mediaType;

    FileType(String extension, MediaType mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public static MediaType getMediaTypeForExtension(String extension) {
        return FILE_TYPE_MEDIA_TYPE_MAP.getOrDefault(extension.toLowerCase(), MediaType.APPLICATION_OCTET_STREAM);
    }

    public static boolean isSupportedExtension(String extension) {
        return FILE_TYPE_MEDIA_TYPE_MAP.containsKey(extension.toLowerCase());
    }
}
