package com.proyecto.Fer.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.proyecto.Fer.model.MenuModel;

public interface MenuRepository extends JpaRepository<MenuModel, Long> {
}