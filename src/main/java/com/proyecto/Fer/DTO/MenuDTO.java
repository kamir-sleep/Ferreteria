package com.proyecto.Fer.DTO;

import lombok.Data;

@Data
public class MenuDTO {
    private Long idMenu;
    private String nombre;
    private String direccion;
    private int estado;
}