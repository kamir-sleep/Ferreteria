package com.proyecto.Fer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.Fer.DTO.CompraDTO;
import com.proyecto.Fer.model.Compra;
import com.proyecto.Fer.service.CompraService;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> getCompraById(@PathVariable Long id) {
        Compra compra = compraService.getCompraById(id);
        return new ResponseEntity<>(compra, HttpStatus.OK);
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<Compra>> getComprasByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<Compra> compras = compraService.getComprasByFecha(inicio, fin);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Compra>> getComprasByProveedor(@PathVariable Long proveedorId) {
        List<Compra> compras = compraService.getComprasByProveedor(proveedorId);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/variante/{varianteId}")
    public ResponseEntity<List<Compra>> getComprasByVariante(@PathVariable Long varianteId) {
        List<Compra> compras = compraService.getComprasByVariante(varianteId);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Compra> createCompra(@RequestBody CompraDTO compraDTO) {
        Compra newCompra = compraService.createCompra(compraDTO);
        return new ResponseEntity<>(newCompra, HttpStatus.CREATED);
    }
}