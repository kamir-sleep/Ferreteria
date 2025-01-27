package com.proyecto.Fer.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambioPasswordDTO {
    @NotBlank(message = "La contraseña actual es requerida")
    private String passwordActual;

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String nuevaPassword;
}