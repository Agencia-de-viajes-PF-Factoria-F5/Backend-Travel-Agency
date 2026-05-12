package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Cloudinary", description = "Gestión de imágenes")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Subir una imagen a Cloudinary")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String url = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/delete/{publicId}")
    @Operation(summary = "Eliminar una imagen de Cloudinary")
    public ResponseEntity<Map<String, String>> deleteImage(
            @PathVariable String publicId
    ) throws IOException {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.ok(Map.of("message", "Imagen eliminada correctamente"));
    }
}