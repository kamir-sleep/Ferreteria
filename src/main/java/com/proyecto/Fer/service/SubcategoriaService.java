package com.proyecto.Fer.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Fer.config.ArchivoService;
import com.proyecto.Fer.model.Subcategoria;
import com.proyecto.Fer.repository.SubcategoriaRepository;

@Service
public class SubcategoriaService {
    
     @Autowired
    private SubcategoriaRepository subcategoriaRepository;
    
    @Autowired
    private ArchivoService archivoService;
    
      public List<Subcategoria> listarSubcategorias() {
        List<Subcategoria> subcategorias = subcategoriaRepository.findAll();
        return subcategorias.stream().map(sub -> {
            if (sub.getImagen() != null && !sub.getImagen().isEmpty()) {
                sub.setImagen(archivoService.getImageUrl(sub.getImagen()));
            }
            return sub;
        }).collect(Collectors.toList());
    }
    
    public List<Subcategoria> listarSubcategoriasPorCategoria(Long categoriaId) {
        List<Subcategoria> subcategorias = subcategoriaRepository.findByCategoriaId(categoriaId);
        return subcategorias.stream().map(sub -> {
            if (sub.getImagen() != null && !sub.getImagen().isEmpty()) {
                sub.setImagen(archivoService.getImageUrl(sub.getImagen()));
            }
            return sub;
        }).collect(Collectors.toList());
    }
    
    public Optional<Subcategoria> obtenerSubcategoriaPorId(Long id) {
        Optional<Subcategoria> subcategoriaOpt = subcategoriaRepository.findById(id);
        if (subcategoriaOpt.isPresent() && subcategoriaOpt.get().getImagen() != null && !subcategoriaOpt.get().getImagen().isEmpty()) {
            Subcategoria subcategoria = subcategoriaOpt.get();
            subcategoria.setImagen(archivoService.getImageUrl(subcategoria.getImagen()));
            return Optional.of(subcategoria);
        }
        return subcategoriaOpt;
    }
    
    
    public Subcategoria guardarSubcategoria(Subcategoria subcategoria) {
        return subcategoriaRepository.save(subcategoria);
    }
    
    public Subcategoria guardarSubcategoriaConImagen(Subcategoria subcategoria, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = archivoService.guardarArchivo(imagen);
            subcategoria.setImagen(nombreArchivo);
        }
        Subcategoria subcategoriaGuardada = subcategoriaRepository.save(subcategoria);
        
        // Asignar URL de la imagen para la respuesta
        if (subcategoriaGuardada.getImagen() != null && !subcategoriaGuardada.getImagen().isEmpty()) {
            subcategoriaGuardada.setImagen(archivoService.getImageUrl(subcategoriaGuardada.getImagen()));
        }
        
        return subcategoriaGuardada;
    }
    
    public Subcategoria actualizarSubcategoria(Long id, Subcategoria subcategoria) {
        Optional<Subcategoria> subcategoriaExistente = subcategoriaRepository.findById(id);
        
        if (subcategoriaExistente.isPresent()) {
            Subcategoria subActual = subcategoriaExistente.get();
            
            // Mantener la misma imagen
            subcategoria.setId(id);
            subcategoria.setImagen(subActual.getImagen());
            
            Subcategoria subcategoriaActualizada = subcategoriaRepository.save(subcategoria);
            
            // Asignar URL de la imagen para la respuesta
            if (subcategoriaActualizada.getImagen() != null && !subcategoriaActualizada.getImagen().isEmpty()) {
                subcategoriaActualizada.setImagen(archivoService.getImageUrl(subcategoriaActualizada.getImagen()));
            }
            
            return subcategoriaActualizada;
        }
        return null;
    }
    
    public Subcategoria actualizarSubcategoriaConImagen(Long id, Subcategoria subcategoria, MultipartFile imagen) throws IOException {
        Optional<Subcategoria> subcategoriaExistente = subcategoriaRepository.findById(id);
        
        if (subcategoriaExistente.isPresent()) {
            Subcategoria subActual = subcategoriaExistente.get();
            
            // Si hay nueva imagen, borrar la anterior y guardar la nueva
            if (imagen != null && !imagen.isEmpty()) {
                if (subActual.getImagen() != null) {
                    archivoService.eliminarArchivo(subActual.getImagen());
                }
                String nombreArchivo = archivoService.guardarArchivo(imagen);
                subcategoria.setImagen(nombreArchivo);
            } else {
                // Mantener la misma imagen si no se proporciona una nueva
                subcategoria.setImagen(subActual.getImagen());
            }
            
            subcategoria.setId(id);
            Subcategoria subcategoriaActualizada = subcategoriaRepository.save(subcategoria);
            
            // Asignar URL de la imagen para la respuesta
            if (subcategoriaActualizada.getImagen() != null && !subcategoriaActualizada.getImagen().isEmpty()) {
                subcategoriaActualizada.setImagen(archivoService.getImageUrl(subcategoriaActualizada.getImagen()));
            }
            
            return subcategoriaActualizada;
        }
        
        return null;
    }
    public void eliminarSubcategoria(Long id) throws IOException {
        Optional<Subcategoria> subcategoria = subcategoriaRepository.findById(id);
        if (subcategoria.isPresent() && subcategoria.get().getImagen() != null) {
            archivoService.eliminarArchivo(subcategoria.get().getImagen());
        }
        subcategoriaRepository.deleteById(id);
    }
}