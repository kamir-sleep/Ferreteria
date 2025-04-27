package com.proyecto.Fer.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
}