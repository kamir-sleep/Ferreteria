package com.proyecto.Fer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.proyecto.Fer.service.RolService;
import com.proyecto.Fer.DTO.RolDTO;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RolController {
    @Autowired
    private RolService rolService;

    @PostMapping
    public ResponseEntity<RolDTO> createRol(@RequestBody RolDTO rolDTO) {
        return ResponseEntity.ok(rolService.createRol(rolDTO));
    }

    @GetMapping("/user-roles")
    public ResponseEntity<Set<RolDTO>> getUserRoles(Authentication authentication) {
        return ResponseEntity.ok(rolService.getRolesByUsuario(authentication.getName()));
    }
}