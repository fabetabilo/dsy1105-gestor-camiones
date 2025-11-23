package com.log_austral.cl.log_austral.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoService {

    private final Path baseDir = Paths.get("/opt/spring/public/camiones");

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacio");
        }
        String original = file.getOriginalFilename();
        String ext = 
            (original != null && original.contains(".")) ? 
                original.substring(original.lastIndexOf('.')).toLowerCase() : 
                ".jpg";
                
        // valida extensiones basicas
        if (!ext.matches("\\.(jpg|jpeg|png|webp)$")) {
            throw new IllegalArgumentException("Extension no permitida (usar jpg/jpeg/png/webp)");
        }
        String filename = UUID.randomUUID() + ext;
        Path target = baseDir.resolve(filename).normalize();
        Files.createDirectories(baseDir);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        
        // ruta relativa que luego se servira estaticamente
        return "/camiones/" + filename;
    }
}
