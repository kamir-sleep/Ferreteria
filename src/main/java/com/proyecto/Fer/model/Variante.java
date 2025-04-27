package com.proyecto.Fer.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@Entity
@NoArgsConstructor

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Unidad de medida en la que se mide el producto (e.g., kg, l, unidades)
    private String unidadMedida; 
    private String codigoBarras;
    private Integer stockTotal;
    private Double PrecioVenta;
    private Double precioCompra; 
    private Boolean activo; // Indica si el producto está activo o no

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonIgnore
    private Producto producto;

    @OneToMany(mappedBy = "variante", cascade = CascadeType.ALL, orphanRemoval = true )
    @ToString.Exclude
    @JsonIgnore
    private List<Lote> lotes = new ArrayList<>();
}