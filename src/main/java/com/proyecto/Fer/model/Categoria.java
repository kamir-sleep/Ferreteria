package com.proyecto.Fer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String imagen;  // URL de la imagen


    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
     @JsonManagedReference // Permite la serialización de las subcategorías en la categoría
private List<Subcategoria> subcategorias = new ArrayList<>();

}