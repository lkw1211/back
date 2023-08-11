package com.boardgaming.api.application.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void putObject(final String fileName, final MultipartFile file);
    void deleteObject(final String fileName);
    String getUrl(final String fileName);
    String getKey(final String url);
    String uploadAndGetUrl(final String fileName, final String url);
}
