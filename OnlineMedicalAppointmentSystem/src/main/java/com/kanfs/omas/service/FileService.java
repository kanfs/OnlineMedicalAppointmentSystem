package com.kanfs.omas.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public String uploadFile(MultipartFile file, String avatar) throws IOException;

    public void removeFile(String fileUrl);
}
