package com.proyecto.Fer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.model.Producto;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.repository.LoteRepository;
import com.proyecto.Fer.repository.ProductoRepository;
import com.proyecto.Fer.repository.VarianteRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class VarianteService {

    private final VarianteRepository varianteRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    @Autowired
    public VarianteService(
            VarianteRepository varianteRepository,
            ProductoRepository productoRepository,
            LoteRepository loteRepository) {
        this.varianteRepository = varianteRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
    }

    public List<Variante> getAllVariantes() {
        return varianteRepository.findAll();
    }

    public Variante getVarianteById(Long id) {
        return varianteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con id: " + id));
    }

    public List<Variante> getVariantesByProducto(Long productoId) {
        return varianteRepository.findByProductoId(productoId);
    }

    public Variante getVarianteByCodigoBarras(String codigoBarras) {
        return varianteRepository.findByCodigoBarras(codigoBarras)
                .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con código de barras: " + codigoBarras));
    }

    public Variante createVariante(Variante variante, Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + productoId));
        
        variante.setProducto(producto);
        variante.setStockTotal(0); // Inicializar stock en 0
        return varianteRepository.save(variante);
    }

    @Transactional
    public Variante updateVariante(Long id, Variante varianteDetails) {
        Variante variante = getVarianteById(id);
        
        variante.setCodigoBarras(varianteDetails.getCodigoBarras());
        variante.setUnidadMedida(varianteDetails.getUnidadMedida());
        variante.setPrecioVenta(varianteDetails.getPrecioVenta());
        variante.setPrecioCompra(varianteDetails.getPrecioCompra());
        variante.setActivo(varianteDetails.getActivo());
        // No actualizamos el stockTotal aquí ya que se calcula automáticamente
        
        return varianteRepository.save(variante);
    }

    @Transactional
    public void updateStockTotal(Long varianteId) {
        // Usamos findByIdForUpdate para bloqueo optimista
        Variante variante = varianteRepository.findByIdForUpdate(varianteId)
                .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con id: " + varianteId));
        
        List<Lote> lotes = loteRepository.findByVarianteId(varianteId);
        
        int stockTotal = lotes.stream()
                .mapToInt(Lote::getCantidad)
                .sum();
        
        variante.setStockTotal(stockTotal);
        varianteRepository.save(variante);
    }

    @Transactional
    public void updatePrecioVenta(Long varianteId) {
        Variante variante = getVarianteById(varianteId);
        List<Lote> lotes = loteRepository.findByVarianteIdOrderByFechaCompraDesc(varianteId);
        
        if (!lotes.isEmpty()) {
            Lote ultimoLote = lotes.get(0);
            variante.setPrecioVenta(ultimoLote.getPrecioVenta());
            varianteRepository.save(variante);
        }
    }

    public void deleteVariante(Long id) {
        Variante variante = getVarianteById(id);
        varianteRepository.delete(variante);
    }

    @Transactional
public Variante cambiarEstadoVariante(Long id, Boolean nuevoEstado) {
    Variante variante = getVarianteById(id);
    variante.setActivo(nuevoEstado);
    return varianteRepository.save(variante);
}

@Transactional
public void updateAllStocks() {
    List<Variante> variantes = varianteRepository.findAll();
    for (Variante variante : variantes) {
        updateStockTotal(variante.getId());
    }
}

}
