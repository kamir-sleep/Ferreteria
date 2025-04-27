package com.proyecto.Fer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {
    private Long varianteId;
    private Integer cantidad;
    private Double precioUnitario;
}
