package com.proyecto.Fer.service;

import com.proyecto.Fer.DTO.MenuDTO;
import com.proyecto.Fer.DTO.RolDTO;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.model.RolModel;
import com.proyecto.Fer.repository.RolRepository;
import com.proyecto.Fer.repository.MenuRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

public interface RolService {
    List<RolModel> findAll();
    Optional<RolModel> findById(Long id);
    RolModel save(RolModel rol);
    void deleteById(Long id);
}