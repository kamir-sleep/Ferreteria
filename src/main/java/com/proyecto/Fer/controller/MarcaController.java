package com.proyecto.Fer.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.model.Marca;
import com.proyecto.Fer.service.MarcaService;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    private final MarcaService marcaService;

    @Autowired
    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<Marca>> getAllMarcas() {
        List<Marca> marcas = marcaService.getAllMarcas();
        return new ResponseEntity<>(marcas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> getMarcaById(@PathVariable Long id) {
        Marca marca = marcaService.getMarcaById(id);
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Marca> createMarca(@RequestBody Marca marca) {
        Marca newMarca = marcaService.createMarca(marca);
        return new ResponseEntity<>(newMarca, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(@PathVariable Long id, @RequestBody Marca marca) {
        Marca updatedMarca = marcaService.updateMarca(id, marca);
        return new ResponseEntity<>(updatedMarca, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Long id) {
        marcaService.deleteMarca(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}