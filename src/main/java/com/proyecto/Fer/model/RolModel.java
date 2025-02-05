package com.proyecto.Fer.model;

import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rol_menu",
        joinColumns = @JoinColumn(name = "id_rol"),
        inverseJoinColumns = @JoinColumn(name = "id_menu")
    )
    private Set<MenuModel> menus = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Set<UsuariosModel> usuarios = new HashSet<>();
}
