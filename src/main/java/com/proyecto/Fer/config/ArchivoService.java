package com.proyecto.Fer.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class ArchivoService {
    @Value("${app.upload.dir:imagenes}")
    private String uploadDir;
    
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        // Crear directorio si no existe
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // Generar nombre único para el archivo
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaCompleta = dirPath.resolve(nombreArchivo);
        
        // Guardar archivo en el sistema de archivos
        Files.copy(archivo.getInputStream(), rutaCompleta);
        
        return nombreArchivo;
    }
    
    public void eliminarArchivo(String nombreArchivo) throws IOException {
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            Path rutaArchivo = Paths.get(uploadDir).resolve(nombreArchivo);
            Files.deleteIfExists(rutaArchivo);
        }
    }

public Resource cargarArchivo(String nombreArchivo) throws IOException {
        Path rutaArchivo = Paths.get(uploadDir).resolve(nombreArchivo);
        Resource recurso = new UrlResource(rutaArchivo.toUri());
        
        if (recurso.exists() && recurso.isReadable()) {
            return recurso;
        } else {
            throw new IOException("No se pudo leer el archivo: " + nombreArchivo);
        }
    }


       
    public String getImageUrl(String nombreArchivo) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/archivos/")
                .path(nombreArchivo)
                .toUriString();
    }
}
