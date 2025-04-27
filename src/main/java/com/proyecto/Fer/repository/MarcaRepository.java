package com.proyecto.Fer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombre(String nombre);
}