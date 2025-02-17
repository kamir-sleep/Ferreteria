package com.proyecto.Fer.repository;

import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.model.RolModel;
import com.proyecto.Fer.model.UsuariosModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<RolModel, Long> {

}