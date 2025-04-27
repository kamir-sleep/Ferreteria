package com.proyecto.Fer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Fer.model.Producto;
import com.proyecto.Fer.service.ProductoService;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    
    @Value("${file.upload-dir:uploads/images}")
    private String uploadDir;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.getProductoById(id);
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    @GetMapping("/subcategoria/{subcategoriaId}")
    public ResponseEntity<List<Producto>> getProductosBySubcategoria(@PathVariable Long subcategoriaId) {
        List<Producto> productos = productoService.getProductosBySubcategoria(subcategoriaId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<Producto>> getProductosByMarca(@PathVariable Long marcaId) {
        List<Producto> productos = productoService.getProductosByMarca(marcaId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> createProducto(
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam("subcategoriaId") Long subcategoriaId,
            @RequestParam(value = "marcaId", required = false) Long marcaId) throws IOException {
        
        Producto newProducto = productoService.createProducto(producto, subcategoriaId, marcaId, imagen);
        return new ResponseEntity<>(newProducto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> updateProducto(
            @PathVariable Long id,
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(value = "subcategoriaId", required = false) Long subcategoriaId,
            @RequestParam(value = "marcaId", required = false) Long marcaId) throws IOException {
        
        Producto updatedProducto = productoService.updateProducto(id, producto, subcategoriaId, marcaId, imagen);
        return new ResponseEntity<>(updatedProducto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) throws IOException {
        productoService.deleteProducto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
