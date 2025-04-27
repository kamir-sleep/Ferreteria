package com.proyecto.Fer.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String imagen;  // URL de la imagen


    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference // Evita la serialización de la categoría en la subcategoría
    private Categoria categoria;

    @OneToMany(mappedBy = "subcategoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();

}