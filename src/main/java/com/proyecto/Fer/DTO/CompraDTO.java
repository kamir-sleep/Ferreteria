package com.proyecto.Fer.DTO;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
    private Long proveedorId;
    private LocalDate fechaCompra;
    private String numeroFactura;
    private List<DetalleCompraDTO> detalles;
}