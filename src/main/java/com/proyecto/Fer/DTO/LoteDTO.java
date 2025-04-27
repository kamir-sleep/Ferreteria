package com.proyecto.Fer.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteDTO {
    private String codigoLote;
    private LocalDate fechaVencimiento;
    private Integer cantidad;
    private Double precioVenta;
}