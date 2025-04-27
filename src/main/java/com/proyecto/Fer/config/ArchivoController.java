package com.proyecto.Fer.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/archivos")
public class ArchivoController {
    
    @Autowired
    private ArchivoService archivoService;
    
    @GetMapping("/{nombreArchivo:.+}")
public ResponseEntity<Resource> obtenerArchivo(@PathVariable String nombreArchivo) {
    try {
        Resource archivo = archivoService.cargarArchivo(nombreArchivo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + archivo.getFilename() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // Caché por 1 hora
                .body(archivo);
    } catch (IOException e) {
        return ResponseEntity.notFound().build();
    }
}
}