package com.proyecto.Fer.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "menus")
public class MenuModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMenu;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "estado")
    private int estado;

    @JsonIgnore //sin recursion
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleMenuMapping> roleMenus = new HashSet<>();

}