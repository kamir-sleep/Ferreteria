package com.proyecto.Fer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

}
