package com.proyecto.Fer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Lote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByVarianteId(Long varianteId);
    Optional<Lote> findByCodigoLote(String codigoLote);
    
    @Query("SELECT l FROM Lote l WHERE l.variante.id = ?1 ORDER BY l.fechaCompra DESC")
    List<Lote> findByVarianteIdOrderByFechaCompraDesc(Long varianteId);
    
    List<Lote> findByFechaCompraBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Lote> findByDetalleCompraId(Long detalleCompraId);
}