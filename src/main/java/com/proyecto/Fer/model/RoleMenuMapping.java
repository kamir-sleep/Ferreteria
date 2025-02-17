package com.proyecto.Fer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "role_menus")
@Data
public class RoleMenuMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore // Evitar la recursión infinita
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "idRol")
    private RolModel rol;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "idMenu")
    private MenuModel menu;
}