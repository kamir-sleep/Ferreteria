package com.proyecto.Fer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.Fer.model.RolModel;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.repository.RolRepository;
import com.proyecto.Fer.repository.MenuRepository;
import com.proyecto.Fer.DTO.RolDTO;
import com.proyecto.Fer.DTO.MenuDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public RolDTO createRol(RolDTO rolDTO) {
        RolModel rol = new RolModel();
        rol.setNombre(rolDTO.getNombre());
        rol.setDescripcion(rolDTO.getDescripcion());
        rol.setEstado(rolDTO.getEstado());
        
        if (rolDTO.getMenuIds() != null) {
            Set<MenuModel> menus = rolDTO.getMenuIds().stream()
                .map(id -> menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu not found")))
                .collect(Collectors.toSet());
            rol.setMenus(menus);
        }
        
        rol = rolRepository.save(rol);
        return convertToDTO(rol);
    }

    public Set<RolDTO> getRolesByUsuario(String login) {
        return rolRepository.findByUsuarios_Login(login).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toSet());
    }

    private RolDTO convertToDTO(RolModel rol) {
        RolDTO dto = new RolDTO();
        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        dto.setEstado(rol.getEstado());
        dto.setMenuIds(rol.getMenus().stream()
            .map(MenuModel::getIdMenu)
            .collect(Collectors.toSet()));
        return dto;
    }
}