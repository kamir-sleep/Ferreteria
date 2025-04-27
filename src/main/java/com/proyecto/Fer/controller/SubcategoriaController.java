package com.proyecto.Fer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Fer.model.Subcategoria;
import com.proyecto.Fer.service.SubcategoriaService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/subcategorias")
public class SubcategoriaController {
     @Autowired
    private SubcategoriaService subcategoriaService;

    @GetMapping
    public List<Subcategoria> listarSubcategorias() {
        return subcategoriaService.listarSubcategorias();
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Subcategoria> listarSubcategoriasPorCategoria(@PathVariable Long categoriaId) {
        return subcategoriaService.listarSubcategoriasPorCategoria(categoriaId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Subcategoria> obtenerSubcategoriaPorId(@PathVariable Long id) {
        return subcategoriaService.obtenerSubcategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Subcategoria> guardarSubcategoria(@RequestBody Subcategoria subcategoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subcategoriaService.guardarSubcategoria(subcategoria));
    }
    
    @PostMapping("/con-imagen")
    public ResponseEntity<Subcategoria> guardarSubcategoriaConImagen(
            @RequestPart("subcategoria") Subcategoria subcategoria,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Subcategoria subcategoriaGuardada = subcategoriaService.guardarSubcategoriaConImagen(subcategoria, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(subcategoriaGuardada);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Subcategoria> actualizarSubcategoria(
            @PathVariable Long id,
            @RequestBody Subcategoria subcategoria) {
        Subcategoria subcategoriaActualizada = subcategoriaService.actualizarSubcategoria(id, subcategoria);
        return subcategoriaActualizada != null ? 
                ResponseEntity.ok(subcategoriaActualizada) : 
                ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/con-imagen")
    public ResponseEntity<Subcategoria> actualizarSubcategoriaConImagen(
            @PathVariable Long id,
            @RequestPart("subcategoria") Subcategoria subcategoria,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Subcategoria subcategoriaActualizada = subcategoriaService.actualizarSubcategoriaConImagen(id, subcategoria, imagen);
            return subcategoriaActualizada != null ? 
                    ResponseEntity.ok(subcategoriaActualizada) : 
                    ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSubcategoria(@PathVariable Long id) {
        try {
            subcategoriaService.eliminarSubcategoria(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


