package com.proyecto.Fer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Fer.model.MenuModel;

public interface MenuRepository extends JpaRepository<MenuModel, Long> {
}