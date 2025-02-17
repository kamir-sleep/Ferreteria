package com.proyecto.Fer.DTO;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String login;
    private int estado;
    private String token;
    private List<RolDTO> roles;

    // Getters y Setters
}

