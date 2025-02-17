package com.proyecto.Fer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Fer.model.RoleMenuMapping;

public interface RoleMenuMappingRepository extends JpaRepository<RoleMenuMapping, Long> {
    List<RoleMenuMapping> findByRolIdRol(Long rolId);
}