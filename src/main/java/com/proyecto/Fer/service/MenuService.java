package com.proyecto.Fer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.DTO.MenuDTO;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.repository.MenuRepository;

import jakarta.transaction.Transactional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public MenuDTO createMenu(MenuDTO menuDTO) {
        MenuModel menu = new MenuModel();
        menu.setNombre(menuDTO.getNombre());
        menu.setDireccion(menuDTO.getDireccion());
        menu.setEstado(menuDTO.getEstado());
        
        menu = menuRepository.save(menu);
        return convertToDTO(menu);
    }

    private MenuDTO convertToDTO(MenuModel menu) {
        MenuDTO dto = new MenuDTO();
        dto.setIdMenu(menu.getIdMenu());
        dto.setNombre(menu.getNombre());
        dto.setDireccion(menu.getDireccion());
        dto.setEstado(menu.getEstado());
        return dto;
    }
}