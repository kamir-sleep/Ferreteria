package com.proyecto.Fer.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.DTO.MenuDTO;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.model.RoleMenuMapping;
import com.proyecto.Fer.repository.RoleMenuMappingRepository;
import com.proyecto.Fer.service.MenuService;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "*")
public class MenuController {
 
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuMappingRepository roleMenuMappingRepository;

    @GetMapping
    public ResponseEntity<List<MenuModel>> getAllMenus() {
        return ResponseEntity.ok(menuService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuModel> getMenuById(@PathVariable Long id) {
        return menuService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MenuModel> createMenu(@RequestBody MenuModel menu) {
        return ResponseEntity.ok(menuService.save(menu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuModel> updateMenu(@PathVariable Long id, @RequestBody MenuModel menu) {
        if (!menuService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        menu.setIdMenu(id);
        return ResponseEntity.ok(menuService.save(menu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        if (!menuService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        menuService.deleteById(id);
        return ResponseEntity.ok().build();
    }


      @GetMapping("/roles/{rolId}")
    public ResponseEntity<List<MenuModel>> getMenusByRole(@PathVariable Long rolId) {
        List<RoleMenuMapping> roleMenus = roleMenuMappingRepository.findByRolIdRol(rolId);
        List<MenuModel> menus = roleMenus.stream()
            .map(RoleMenuMapping::getMenu)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(menus);
    }
}