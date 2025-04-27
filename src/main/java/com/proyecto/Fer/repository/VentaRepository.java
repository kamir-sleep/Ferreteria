package com.proyecto.Fer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findById(Long id);
    Optional<Venta> findByCodigoVenta(String codigoVenta);
    List<Venta> findByEmpleadoId(Long empleadoId);
    List<Venta> findByClienteIdCliente(Long clienteId);

    List<Venta> findByCiCliente(String ciCliente);

    @Query("SELECT COUNT(v) FROM Venta v WHERE FUNCTION('DATE', v.fechaVenta) = CURRENT_DATE")
long contarVentasHoy();

}