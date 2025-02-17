package com.proyecto.Fer.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.Fer.model.RolModel;
import com.proyecto.Fer.repository.RolRepository;
import com.proyecto.Fer.service.RolService;
import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<RolModel> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<RolModel> findById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public RolModel save(RolModel rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteById(Long id) {
        rolRepository.deleteById(id);
    }
}