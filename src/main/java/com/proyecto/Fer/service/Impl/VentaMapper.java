package com.proyecto.Fer.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proyecto.Fer.DTO.DetalleVentaDTO;
import com.proyecto.Fer.DTO.VentaDTO;
import com.proyecto.Fer.model.DetalleVenta;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.model.Venta;
import com.proyecto.Fer.repository.ClienteRepository;
import com.proyecto.Fer.repository.EmpleadoRepository;
import com.proyecto.Fer.repository.LoteRepository;
import com.proyecto.Fer.repository.VarianteRepository;
import com.proyecto.Fer.service.FEFOStockService;

import org.springframework.transaction.annotation.Transactional;

@Component
public class VentaMapper {

    @Autowired
    private VarianteRepository varianteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

       @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private FEFOStockService fefoStockService;

    @Transactional
    public Venta toEntity(VentaDTO dto) {
        Venta venta = new Venta();
        venta.setCodigoVenta(dto.getCodigoVenta());
        venta.setTotalPagado(dto.getTotalPagado());
        venta.setCiCliente(dto.getCiCliente());
        
        if (dto.getEmpleadoId() != null) {
            venta.setEmpleado(empleadoRepository.findById(dto.getEmpleadoId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado")));
        }
    
        if (dto.getClienteId() != null) {
            venta.setCliente(clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        }
    
        List<DetalleVenta> detalles = new ArrayList<>();
        double totalVenta = 0.0;
        
        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            Variante variante = varianteRepository.findById(detalleDTO.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
            
            // Verificar stock suficiente
            if (variante.getStockTotal() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para variante con ID: " + variante.getId());
            }
            
            // Aplicar FEFO y reducir stock
            fefoStockService.reducirStockFEFO(variante.getId(), detalleDTO.getCantidad());
            
            // Crear el detalle de venta
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVariante(variante);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(variante.getPrecioVenta());
            detalle.calcularSubtotal();
            detalle.setVenta(venta);
            
            detalles.add(detalle);
            totalVenta += detalle.getSubtotal();
        }
    
        venta.setDetalles(detalles);
        venta.setTotal(totalVenta);
        
        // Calculando el total pagado
        if (venta.getTotalPagado() == null) {
            venta.setTotalPagado(venta.getTotal()); // Default to total if not specified
        } else if (venta.getTotalPagado() < venta.getTotal()) {
            throw new RuntimeException("El total pagado no puede ser menor al total de la venta.");
        }
        
        return venta;
    }


    public VentaDTO toDto(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setCodigoVenta(venta.getCodigoVenta());
        dto.setCiCliente(venta.getCiCliente());
        dto.setTotalPagado(venta.getTotalPagado());

        if (venta.getEmpleado() != null) dto.setEmpleadoId(venta.getEmpleado().getId());
        if (venta.getCliente() != null) dto.setClienteId(venta.getCliente().getIdCliente());

        List<DetalleVentaDTO> detalles = venta.getDetalles().stream().map(d -> {
            DetalleVentaDTO dDto = new DetalleVentaDTO();
            dDto.setVarianteId(d.getVariante().getId());
            dDto.setCantidad(d.getCantidad());
            dDto.setPrecioUnitario(d.getPrecioUnitario());
            return dDto;
        }).toList();

        dto.setDetalles(detalles);

        return dto;
    }

}
