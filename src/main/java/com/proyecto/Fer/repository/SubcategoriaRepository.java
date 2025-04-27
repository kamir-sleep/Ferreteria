package com.proyecto.Fer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Subcategoria;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {
    List<Subcategoria> findByCategoriaId(Long categoriaId);
   
    Optional<Subcategoria> findByNombre(String nombre);
}