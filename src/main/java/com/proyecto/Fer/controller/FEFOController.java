package com.proyecto.Fer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.service.FEFOStockService;

@RestController
@RequestMapping("/api/fefo")
@CrossOrigin(origins = "*")
public class FEFOController {

    @Autowired
    private FEFOStockService fefoStockService;
    
    @GetMapping("/lotes/{varianteId}")
    public ResponseEntity<List<Lote>> getLotesPorFEFO(@PathVariable Long varianteId) {
        List<Lote> lotes = fefoStockService.obtenerLotesPorFEFO(varianteId);
        return ResponseEntity.ok(lotes);
    }
}