package com.proyecto.Fer.service.Impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.DetalleVenta;
import com.proyecto.Fer.model.Variante;
import com.proyecto.Fer.model.Venta;
import com.proyecto.Fer.repository.LoteRepository;
import com.proyecto.Fer.repository.VentaRepository;
import com.proyecto.Fer.service.FEFOStockService;
import com.proyecto.Fer.service.VentaService;

import org.springframework.transaction.annotation.Transactional;
@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
     
    @Autowired
    private LoteRepository loteRepository;
    
    @Autowired
    private FEFOStockService fefoStockService;


    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
// En VentaServiceImpl
@Override
@Transactional
public Venta crearVenta(Venta venta) {
    // Verificamos cada variante para stock
    for (DetalleVenta detalle : venta.getDetalles()) {
        Variante variante = detalle.getVariante();

        if (variante.getStockTotal() < detalle.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para variante con ID: " + variante.getId());
        }

        // ELIMINAR ESTA LÍNEA para evitar reducción duplicada:
        // fefoStockService.reducirStockFEFO(variante.getId(), detalle.getCantidad());
    }

    // El resto del código se mantiene igual...
    double total = venta.getDetalles().stream()
            .mapToDouble(DetalleVenta::getSubtotal)
            .sum();
    venta.setTotal(total);

    if (venta.getCliente() == null && (venta.getCiCliente() == null || venta.getCiCliente().isEmpty())) {
        throw new RuntimeException("Debe proporcionar el CI del cliente si no se relaciona uno.");
    }

    venta.getDetalles().forEach(d -> d.setVenta(venta));

    return ventaRepository.save(venta);
}
    @Override
    public Venta editarVenta(Long id, Venta ventaActualizada) {
        Venta venta = obtenerPorId(id);

        venta.setDetalles(ventaActualizada.getDetalles());
        venta.setCliente(ventaActualizada.getCliente());
        venta.setEmpleado(ventaActualizada.getEmpleado());
        venta.setCiCliente(ventaActualizada.getCiCliente());
        venta.setTotal(ventaActualizada.getTotal());
        venta.getDetalles().forEach(d -> d.setVenta(venta));

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminarVenta(Long id) {
        Venta venta = obtenerPorId(id);
        venta.setActivo(false);
        ventaRepository.save(venta);
    }


}
