package com.proyecto.Fer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.Fer.model.Proveedor;
import com.proyecto.Fer.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> getAllProveedores() {
        List<Proveedor> proveedores = proveedorService.getAllProveedores();
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Long id) {
        Proveedor proveedor = proveedorService.getProveedorById(id);
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Proveedor>> searchProveedores(@RequestParam String nombre) {
        List<Proveedor> proveedores = proveedorService.searchProveedoresByNombre(nombre);
        return new ResponseEntity<>(proveedores, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proveedor> createProveedor(@RequestBody Proveedor proveedor) {
        Proveedor newProveedor = proveedorService.createProveedor(proveedor);
        return new ResponseEntity<>(newProveedor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(
            @PathVariable Long id,
            @RequestBody Proveedor proveedor) {
        
        Proveedor updatedProveedor = proveedorService.updateProveedor(id, proveedor);
        return new ResponseEntity<>(updatedProveedor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteProveedor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}