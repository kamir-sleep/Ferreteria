package com.proyecto.Fer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.Marca;
import com.proyecto.Fer.repository.MarcaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public List<Marca> getAllMarcas() {
        return marcaRepository.findAll();
    }

    public Marca getMarcaById(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con id: " + id));
    }

    public Marca createMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    public Marca updateMarca(Long id, Marca marcaDetails) {
        Marca marca = getMarcaById(id);
        marca.setNombre(marcaDetails.getNombre());
        return marcaRepository.save(marca);
    }

    public void deleteMarca(Long id) {
        Marca marca = getMarcaById(id);
        marcaRepository.delete(marca);
    }
}