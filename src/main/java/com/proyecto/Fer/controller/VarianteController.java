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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.service.VarianteService;

@RestController
@RequestMapping("/api/variantes")
@CrossOrigin(origins = "*")
public class VarianteController {

    private final VarianteService varianteService;

    @Autowired
    public VarianteController(VarianteService varianteService) {
        this.varianteService = varianteService;
    }

    @GetMapping
    public ResponseEntity<List<Variante>> getAllVariantes() {
        List<Variante> variantes = varianteService.getAllVariantes();
        return new ResponseEntity<>(variantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variante> getVarianteById(@PathVariable Long id) {
        Variante variante = varianteService.getVarianteById(id);
        return new ResponseEntity<>(variante, HttpStatus.OK);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Variante>> getVariantesByProducto(@PathVariable Long productoId) {
        List<Variante> variantes = varianteService.getVariantesByProducto(productoId);
        return new ResponseEntity<>(variantes, HttpStatus.OK);
    }

    @GetMapping("/codigo-barras/{codigoBarras}")
    public ResponseEntity<Variante> getVarianteByCodigoBarras(@PathVariable String codigoBarras) {
        Variante variante = varianteService.getVarianteByCodigoBarras(codigoBarras);
        return new ResponseEntity<>(variante, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Variante> createVariante(
            @RequestBody Variante variante,
            @RequestParam("productoId") Long productoId) {

        Variante newVariante = varianteService.createVariante(variante, productoId);
        return new ResponseEntity<>(newVariante, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variante> updateVariante(
            @PathVariable Long id,
            @RequestBody Variante variante) {

        Variante updatedVariante = varianteService.updateVariante(id, variante);
        return new ResponseEntity<>(updatedVariante, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariante(@PathVariable Long id) {
        varianteService.deleteVariante(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Variante> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo) {

        Variante varianteActualizada = varianteService.cambiarEstadoVariante(id, activo);
        return new ResponseEntity<>(varianteActualizada, HttpStatus.OK);
    }

    @PutMapping("/actualizar-stocks")
    public ResponseEntity<Void> actualizarTodosLosStocks() {
        varianteService.updateAllStocks();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
