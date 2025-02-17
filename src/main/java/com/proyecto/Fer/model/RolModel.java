package com.proyecto.Fer.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class RolModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private int estado;
   
    @JsonIgnore // Evitar la recursión infinita
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleMapping> userRoles = new HashSet<>();

    @JsonIgnore // Evitar la recursión infinita
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleMenuMapping> roleMenus = new HashSet<>();

}
