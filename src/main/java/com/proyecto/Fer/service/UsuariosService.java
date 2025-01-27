package com.proyecto.Fer.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.Fer.DTO.UsuariosDTO;
import com.proyecto.Fer.model.UsuariosModel;

public interface UsuariosService {
	List<UsuariosDTO> findByUsuarios();
		Optional<UsuariosModel> obtenerUsuarioPorLogin(String login);
    
}
