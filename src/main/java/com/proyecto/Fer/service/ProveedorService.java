package com.proyecto.Fer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.Proveedor;
import com.proyecto.Fer.repository.ProveedorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor getProveedorById(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + id));
    }

    public List<Proveedor> searchProveedoresByNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Proveedor createProveedor(Proveedor proveedor) {
        // Validar que el nombre no exista
        if (proveedorRepository.existsByNombre(proveedor.getNombre())) {
            throw new IllegalArgumentException("Ya existe un proveedor con el nombre: " + proveedor.getNombre());
        }
        return proveedorRepository.save(proveedor);
    }

    public Proveedor updateProveedor(Long id, Proveedor proveedorDetails) {
        Proveedor proveedor = getProveedorById(id);
        
        // Validar que el nombre no exista si se está cambiando
        if (!proveedor.getNombre().equals(proveedorDetails.getNombre()) && 
            proveedorRepository.existsByNombre(proveedorDetails.getNombre())) {
            throw new IllegalArgumentException("Ya existe un proveedor con el nombre: " + proveedorDetails.getNombre());
        }
        
        proveedor.setNombre(proveedorDetails.getNombre());
        proveedor.setDireccion(proveedorDetails.getDireccion());
        proveedor.setTelefono(proveedorDetails.getTelefono());
        
        return proveedorRepository.save(proveedor);
    }

    public void deleteProveedor(Long id) {
        Proveedor proveedor = getProveedorById(id);
        proveedorRepository.delete(proveedor);
    }
}