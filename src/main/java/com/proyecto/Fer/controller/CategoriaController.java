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

import com.proyecto.Fer.model.Categoria;
import com.proyecto.Fer.service.CategoriaService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }
    
     
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerCategoriaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Categoria> guardarCategoria(@RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.guardarCategoria(categoria));
    }
  @PostMapping("/con-imagen")
    public ResponseEntity<Categoria> guardarCategoriaConImagen(
            @RequestPart("categoria") Categoria categoria,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Categoria categoriaGuardada = categoriaService.guardarCategoriaConImagen(categoria, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody Categoria categoria) {
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, categoria);
        return categoriaActualizada != null ? 
                ResponseEntity.ok(categoriaActualizada) : 
                ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/con-imagen")
    public ResponseEntity<Categoria> actualizarCategoriaConImagen(
            @PathVariable Long id,
            @RequestPart("categoria") Categoria categoria,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Categoria categoriaActualizada = categoriaService.actualizarCategoriaConImagen(id, categoria, imagen);
            return categoriaActualizada != null ? 
                    ResponseEntity.ok(categoriaActualizada) : 
                    ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
