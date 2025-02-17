package com.proyecto.Fer.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.proyecto.Fer.model.MenuModel;


public interface MenuService {
    List<MenuModel> findAll();
    Optional<MenuModel> findById(Long id);
    MenuModel save(MenuModel menu);
    void deleteById(Long id);
}