package com.proyecto.Fer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Variante;

import jakarta.persistence.LockModeType;

@Repository
public interface VarianteRepository extends JpaRepository<Variante, Long> {
    List<Variante> findByProductoId(Long productoId);
    Optional<Variante> findByCodigoBarras(String codigoBarras);
    
    /*@Query("SELECT v FROM Variante v WHERE v.producto.id = ?1 AND v.cantidad = ?2")
    List<Variante> findByProductoIdAndCantidad(Long productoId, Double cantidad);
     */

   /*  @Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT v FROM Variante v WHERE v.id = :id")
Optional<Variante> findByIdForUpdate(@Param("id") Long id);
*/
@Query(value = "SELECT * FROM variante WHERE id = :id FOR UPDATE", nativeQuery = true)
Optional<Variante> findByIdForUpdate(@Param("id") Long id);


}
