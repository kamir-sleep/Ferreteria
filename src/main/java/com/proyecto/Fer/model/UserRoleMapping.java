package com.proyecto.Fer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_roles")
@Data
public class UserRoleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "login", referencedColumnName = "login")
    private UsuariosModel usuario;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "idRol")
    private RolModel rol;
}