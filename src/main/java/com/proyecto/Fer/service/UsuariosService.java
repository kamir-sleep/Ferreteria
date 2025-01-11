package com.proyecto.Fer.service;

import java.util.List;
import java.util.Optional;

import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.model.DTO.UsuariosDTO;

public interface UsuariosService {
	List<UsuariosDTO> findByUsuarios();
		Optional<UsuariosModel> obtenerUsuarioPorLogin(String login);
    
}
