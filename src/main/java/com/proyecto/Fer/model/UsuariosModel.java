package com.proyecto.Fer.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "usuario_model")
public class UsuariosModel {
	@Id
	@Column(name = "login", unique = true)
	String login;

	@Column(name = "passwd")
	String passwd;

	@Column(name = "estado")
	int estado;

	
	@Column(name="token")
	String token;
	
 @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Cliente cliente;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Empleado empleado;

	// Modificación en UsuariosModel.java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
    name = "usuario_rol",
    joinColumns = @JoinColumn(name = "usuario_login"),
    inverseJoinColumns = @JoinColumn(name = "id_rol")
)
private Set<RolModel> roles = new HashSet<>();

}
