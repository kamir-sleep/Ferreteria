package com.proyecto.Fer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Fer.model.UserRoleMapping;

public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
    List<UserRoleMapping> findByUsuarioLogin(String login);
}