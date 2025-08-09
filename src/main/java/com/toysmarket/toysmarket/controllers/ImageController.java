package com.toysmarket.toysmarket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

//ВАЖНО Resource должен быть именно отсюда, а не из jakarta
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ResourceLoader resourceLoader;

    private final Path rootLocation = Path.of("uploads/images");
    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");

    //Получение изображения
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename){
        try{
            Path filePath = Paths.get("uploads/images/" + filename).toAbsolutePath();

            Resource resource = new UrlResource(filePath.toUri());

            String contentType = determineContentType(filename);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> "application/octet-stream";
        };
    }
}
