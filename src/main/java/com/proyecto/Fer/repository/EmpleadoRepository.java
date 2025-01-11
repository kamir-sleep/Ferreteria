package com.proyecto.Fer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Fer.model.Empleado;

// Repositorio
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {}
