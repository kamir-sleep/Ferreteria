package com.proyecto.Fer.service;

import java.util.List;

import com.proyecto.Fer.model.Venta;

public interface VentaService {
    List<Venta> listarVentas();
    Venta obtenerPorId(Long id);
    Venta crearVenta(Venta venta);
    Venta editarVenta(Long id, Venta venta);
    void eliminarVenta(Long id);
}
