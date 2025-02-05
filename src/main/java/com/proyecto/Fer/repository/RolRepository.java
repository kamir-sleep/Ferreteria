package com.proyecto.Fer.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Fer.model.RolModel;

public interface RolRepository extends JpaRepository<RolModel, Long> {
    Set<RolModel> findByUsuarios_Login(String login);
}
