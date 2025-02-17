package com.proyecto.Fer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.proyecto.Fer.service.RolService;
import com.proyecto.Fer.DTO.MenuDTO;
import com.proyecto.Fer.DTO.RolDTO;
import com.proyecto.Fer.model.RolModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {


    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolModel>> getAllRoles() {
        return ResponseEntity.ok(rolService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolModel> getRolById(@PathVariable Long id) {
        return rolService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RolModel> createRol(@RequestBody RolModel rol) {
        return ResponseEntity.ok(rolService.save(rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolModel> updateRol(@PathVariable Long id, @RequestBody RolModel rol) {
        if (!rolService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rol.setIdRol(id);
        return ResponseEntity.ok(rolService.save(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRol(@PathVariable Long id) {
        if (!rolService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        rolService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}