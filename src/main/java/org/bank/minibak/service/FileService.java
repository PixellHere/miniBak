package org.bank.minibak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadDir;

    public FileService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String saveFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir).resolve(fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }

    public Resource getFile(String filename) {
        try {
            if (filename.contains("..")) {
                throw new RuntimeException("Invalid filename");
            }

            Path path = Paths.get(uploadDir).resolve(filename);

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("File error");
        }
    }

    public String getContentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    public Path getFilePath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }
}
