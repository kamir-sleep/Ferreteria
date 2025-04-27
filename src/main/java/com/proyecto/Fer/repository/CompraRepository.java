package com.proyecto.Fer.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByFechaCompraBetween(LocalDate inicio, LocalDate fin);
    List<Compra> findByProveedorId(Long proveedorId);
    
    @Query("SELECT c FROM Compra c JOIN c.detalles d WHERE d.variante.id = :varianteId")
    List<Compra> findByVarianteId(@Param("varianteId") Long varianteId);
}