package com.proyecto.Fer.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySubcategoriaId(Long subcategoriaId);
    List<Producto> findByMarcaId(Long marcaId);
    List<Producto> findByEstado(Boolean estado);
    
    
}