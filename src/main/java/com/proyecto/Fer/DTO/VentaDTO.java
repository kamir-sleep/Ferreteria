package com.proyecto.Fer.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long id;
    private String codigoVenta;
    private Long empleadoId;
    private Long clienteId;
    private String ciCliente;
    private Double totalPagado;
    private List<DetalleVentaDTO> detalles;
}
