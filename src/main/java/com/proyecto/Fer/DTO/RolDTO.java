package com.proyecto.Fer.DTO;

import lombok.Data;
import java.util.Set;

@Data
public class RolDTO {
    private Long idRol;
    private String nombre;
    private String descripcion;
    private int estado;
    private Set<Long> menuIds;
}
