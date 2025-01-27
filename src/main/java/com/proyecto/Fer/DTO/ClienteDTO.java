package com.proyecto.Fer.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ClienteDTO {
    private Long idCliente;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 6, max = 20, message = "El documento debe tener entre 6 y 20 caracteres")
    private String documentoIdentidad;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
    private String direccion;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{8,14}$", message = "Número de teléfono inválido")
    private String telefono;

    @NotBlank
    @Email(message = "Formato de email inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    private String genero;
    private LocalDate fechaRegistro;
    private int estado;
    private String nit;
}