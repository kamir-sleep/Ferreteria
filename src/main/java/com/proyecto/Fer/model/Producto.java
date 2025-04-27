package com.proyecto.Fer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String imagen;
    private Boolean estado;
    private String tipoMedia; // atributo para clasificar productos (ej: Líquidos, Herramientas, etc.)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private Subcategoria subcategoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<Variante> variantes = new ArrayList<>();

}