package com.proyecto.Fer.service;

import java.util.List;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.repository.LoteRepository;
import com.proyecto.Fer.repository.VarianteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FEFOStockService {

    private final LoteRepository loteRepository;
    private final VarianteRepository varianteRepository;

    @Autowired
    public FEFOStockService(LoteRepository loteRepository, VarianteRepository varianteRepository) {
        this.loteRepository = loteRepository;
        this.varianteRepository = varianteRepository;
    }

    /**
     * Reduce el stock de una variante aplicando el principio FEFO (First Expired, First Out)
     * @param varianteId ID de la variante
     * @param cantidadSolicitada Cantidad a reducir
     * @return Map con los lotes utilizados y sus cantidades
     */
    @Transactional
    public Map<Long, Integer> reducirStockFEFO(Long varianteId, int cantidadSolicitada) {
        // Obtener la variante con bloqueo para actualización
        Variante variante = varianteRepository.findByIdForUpdate(varianteId)
                .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con id: " + varianteId));
        
        // Verificar si hay suficiente stock
        if (variante.getStockTotal() < cantidadSolicitada) {
            throw new RuntimeException("Stock insuficiente para variante con ID: " + varianteId);
        }
        
        // Obtener todos los lotes de la variante que tengan stock > 0
        List<Lote> lotes = loteRepository.findByVarianteId(varianteId).stream()
                .filter(lote -> lote.getCantidad() > 0)
                .sorted(Comparator.comparing(Lote::getFechaVencimiento)) // Ordenar por fecha de vencimiento (ascendente)
                .toList();
        
        // Mapa para registrar los lotes utilizados y sus cantidades
        Map<Long, Integer> lotesUtilizados = new HashMap<>();
        int cantidadRestante = cantidadSolicitada;
        
        // Recorrer los lotes ordenados por fecha de vencimiento y reducir stock
        for (Lote lote : lotes) {
            if (cantidadRestante <= 0) break;
            
            int cantidadDisponible = lote.getCantidad();
            int cantidadAReducir = Math.min(cantidadDisponible, cantidadRestante);
            
            // Actualizar la cantidad del lote
            lote.setCantidad(cantidadDisponible - cantidadAReducir);
            loteRepository.save(lote);
            
            // Registrar el lote utilizado
            lotesUtilizados.put(lote.getId(), cantidadAReducir);
            cantidadRestante -= cantidadAReducir;
        }
        
        // Verificar si se pudo satisfacer toda la cantidad solicitada
        if (cantidadRestante > 0) {
            throw new RuntimeException("No se pudo satisfacer la cantidad solicitada con los lotes disponibles");
        }
        
        // Actualizar el stock total de la variante
        variante.setStockTotal(variante.getStockTotal() - cantidadSolicitada);
        varianteRepository.save(variante);
        
        return lotesUtilizados;
    }
    
    /**
     * Obtiene los lotes disponibles para una variante, ordenados por fecha de vencimiento
     * @param varianteId ID de la variante
     * @return Lista de lotes ordenados por fecha de vencimiento
     */
    public List<Lote> obtenerLotesPorFEFO(Long varianteId) {
        return loteRepository.findByVarianteId(varianteId).stream()
                .filter(lote -> lote.getCantidad() > 0)
                .sorted(Comparator.comparing(Lote::getFechaVencimiento))
                .toList();
    }
}