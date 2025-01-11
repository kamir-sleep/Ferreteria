package com.proyecto.Fer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "usuario_model")
public class UsuariosModel {
	@Id
	@Column(name = "login")
	String login;

	@Column(name = "passwd")
	String passwd;

	@Column(name = "estado")
	int estado;

	
	@Column(name="token")
	String token;
	


}
