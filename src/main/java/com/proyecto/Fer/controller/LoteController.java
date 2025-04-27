package com.proyecto.Fer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.service.LoteService;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

   
    private final LoteService loteService;

    @Autowired
    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public ResponseEntity<List<Lote>> getAllLotes() {
        List<Lote> lotes = loteService.getAllLotes();
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> getLoteById(@PathVariable Long id) {
        Lote lote = loteService.getLoteById(id);
        return new ResponseEntity<>(lote, HttpStatus.OK);
    }

    @GetMapping("/variante/{varianteId}")
    public ResponseEntity<List<Lote>> getLotesByVariante(@PathVariable Long varianteId) {
        List<Lote> lotes = loteService.getLotesByVariante(varianteId);
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<Lote>> getLotesByFechaCompra(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Lote> lotes = loteService.getLotesByFechaCompra(fechaInicio, fechaFin);
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/codigo-lote/{codigoLote}")
    public ResponseEntity<Lote> getLoteByCodigoLote(@PathVariable String codigoLote) {
        Lote lote = loteService.getLoteByCodigoLote(codigoLote);
        return new ResponseEntity<>(lote, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Lote> createLote(
            @RequestBody Lote lote,
            @RequestParam("varianteId") Long varianteId,
            @RequestParam(value = "detalleCompraId", required = false) Long detalleCompraId) {
        
        Lote newLote = loteService.createLote(lote, varianteId, detalleCompraId);
        return new ResponseEntity<>(newLote, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> updateLote(
            @PathVariable Long id,
            @RequestBody Lote lote) {
        
        Lote updatedLote = loteService.updateLote(id, lote);
        return new ResponseEntity<>(updatedLote, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLote(@PathVariable Long id) {
        loteService.deleteLote(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}