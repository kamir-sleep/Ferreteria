package com.proyecto.Fer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Proveedor;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
}