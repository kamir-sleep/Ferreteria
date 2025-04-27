package com.proyecto.Fer.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.Fer.DTO.CompraDTO;
import com.proyecto.Fer.DTO.DetalleCompraDTO;
import com.proyecto.Fer.DTO.LoteDTO;
import com.proyecto.Fer.model.Compra;
import com.proyecto.Fer.model.DetalleCompra;
import com.proyecto.Fer.model.Lote;
import com.proyecto.Fer.model.Proveedor;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.repository.CompraRepository;
import com.proyecto.Fer.repository.DetalleCompraRepository;
import com.proyecto.Fer.repository.ProveedorRepository;
import com.proyecto.Fer.repository.VarianteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final VarianteRepository varianteRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final VarianteService varianteService;

    @Autowired
    public CompraService(
            CompraRepository compraRepository,
            ProveedorRepository proveedorRepository,
            VarianteRepository varianteRepository,
            DetalleCompraRepository detalleCompraRepository,
            VarianteService varianteService) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.varianteRepository = varianteRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.varianteService = varianteService;
    }

    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    public Compra getCompraById(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con id: " + id));
    }

    public List<Compra> getComprasByFecha(LocalDate inicio, LocalDate fin) {
        return compraRepository.findByFechaCompraBetween(inicio, fin);
    }

    public List<Compra> getComprasByProveedor(Long proveedorId) {
        return compraRepository.findByProveedorId(proveedorId);
    }

    public List<Compra> getComprasByVariante(Long varianteId) {
        return compraRepository.findByVarianteId(varianteId);
    }

    @Transactional
    public Compra createCompra(CompraDTO compraDTO) {
        Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + compraDTO.getProveedorId()));
        
        Compra compra = new Compra();
        compra.setFechaCompra(compraDTO.getFechaCompra() != null ? compraDTO.getFechaCompra() : LocalDate.now());
        compra.setProveedor(proveedor);
        compra.setNumeroFactura(compraDTO.getNumeroFactura());
        compra.setDetalles(new ArrayList<>());
        
        Double total = 0.0;
        
        for (DetalleCompraDTO detalleDTO : compraDTO.getDetalles()) {
            Variante variante = varianteRepository.findByIdForUpdate(detalleDTO.getVarianteId())
                    .orElseThrow(() -> new EntityNotFoundException("Variante no encontrada con id: " + detalleDTO.getVarianteId()));
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setVariante(variante);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setSubtotal(detalleDTO.getCantidad() * detalleDTO.getPrecioUnitario());
            detalle.setLotes(new ArrayList<>());
            
            total += detalle.getSubtotal();
            
            for (LoteDTO loteDTO : detalleDTO.getLotes()) {
                Lote lote = new Lote();
                lote.setCodigoLote(loteDTO.getCodigoLote());
                lote.setFechaVencimiento(loteDTO.getFechaVencimiento());
                lote.setCantidad(loteDTO.getCantidad());
                lote.setPrecioCompra(detalleDTO.getPrecioUnitario());
                lote.setPrecioVenta(loteDTO.getPrecioVenta());
                lote.setVariante(variante);
                lote.setDetalleCompra(detalle);
                
                detalle.getLotes().add(lote);
            }
            
            compra.getDetalles().add(detalle);
        }
        
        compra.setTotal(total);
        Compra savedCompra = compraRepository.save(compra);
        
        // Actualizar stock y precios de las variantes
        for (DetalleCompra detalle : savedCompra.getDetalles()) {
            varianteService.updateStockTotal(detalle.getVariante().getId());
            varianteService.updatePrecioVenta(detalle.getVariante().getId());
        }
        
        return savedCompra;
    }
}