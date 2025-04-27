package com.proyecto.Fer.DTO;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraDTO {
    private Long varianteId;
    private Integer cantidad;
    private Double precioUnitario;
    private List<LoteDTO> lotes;
}