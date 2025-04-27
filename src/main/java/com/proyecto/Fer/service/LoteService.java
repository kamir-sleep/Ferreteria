package com.proyecto.Fer.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.DetalleCompra;
import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.repository.DetalleCompraRepository;
import com.proyecto.Fer.repository.LoteRepository;
import com.proyecto.Fer.repository.VarianteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class LoteService {

   
    private final LoteRepository loteRepository;
    private final VarianteRepository varianteRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final VarianteService varianteService;

    @Autowired
    public LoteService(
            LoteRepository loteRepository,
            VarianteRepository varianteRepository,
            DetalleCompraRepository detalleCompraRepository,
            VarianteService varianteService) {
        this.loteRepository = loteRepository;
        this.varianteRepository = varianteRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.varianteService = varianteService;
    }

    public List<Lote> getAllLotes() {
        return loteRepository.findAll();
    }

    public Lote getLoteById(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lote no encontrado con id: " + id));
    }

    public List<Lote> getLotesByVariante(Long varianteId) {
        return loteRepository.findByVarianteId(varianteId);
    }

    public List<Lote> getLotesByFechaCompra(LocalDate fechaInicio, LocalDate fechaFin) {
        return loteRepository.findByFechaCompraBetween(fechaInicio, fechaFin);
    }

    public Lote getLoteByCodigoLote(String codigoLote) {
        return loteRepository.findByCodigoLote(codigoLote)
                .orElseThrow(() -> new EntityNotFoundException("Lote no encontrado con código: " + codigoLote));
    }

    @Transactional
    public Lote createLote(Lote lote, Long varianteId, Long detalleCompraId) {
        Variante variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con id: " + varianteId));
        
        DetalleCompra detalleCompra = null;
        if (detalleCompraId != null) {
            detalleCompra = detalleCompraRepository.findById(detalleCompraId)
                    .orElseThrow(() -> new EntityNotFoundException("Detalle de compra no encontrado con id: " + detalleCompraId));
        }
        
        lote.setVariante(variante);
        lote.setDetalleCompra(detalleCompra);
        
        // Si no se especifica la fecha de compra, se usa la fecha actual
        if (lote.getFechaCompra() == null) {
            lote.setFechaCompra(LocalDate.now());
        }
        
        Lote savedLote = loteRepository.save(lote);
        
        // Actualizar stock total de la variante
        varianteService.updateStockTotal(varianteId);
        
        // Actualizar precio de venta de la variante con el último lote
        varianteService.updatePrecioVenta(varianteId);
        
        return savedLote;
    }

    @Transactional
    public Lote updateLote(Long id, Lote loteDetails) {
        Lote lote = getLoteById(id);
        Long varianteId = lote.getVariante().getId();
        
        lote.setCodigoLote(loteDetails.getCodigoLote());
        
        if (loteDetails.getFechaCompra() != null) {
            lote.setFechaCompra(loteDetails.getFechaCompra());
        }
        
        lote.setFechaVencimiento(loteDetails.getFechaVencimiento());
        lote.setPrecioCompra(loteDetails.getPrecioCompra());
        lote.setPrecioVenta(loteDetails.getPrecioVenta());
        lote.setCantidad(loteDetails.getCantidad());
        
        Lote updatedLote = loteRepository.save(lote);
        
        // Actualizar stock total de la variante
        varianteService.updateStockTotal(varianteId);
        
        // Actualizar precio de venta de la variante
        varianteService.updatePrecioVenta(varianteId);
        
        return updatedLote;
    }

    @Transactional
    public void deleteLote(Long id) {
        Lote lote = getLoteById(id);
        Long varianteId = lote.getVariante().getId();
        
        loteRepository.delete(lote);
        
        // Actualizar stock total de la variante
        varianteService.updateStockTotal(varianteId);
    }

}
