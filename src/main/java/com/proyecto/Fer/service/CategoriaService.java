package com.proyecto.Fer.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Fer.config.ArchivoService;
import com.proyecto.Fer.model.Categoria;
import com.proyecto.Fer.model.Subcategoria;
import com.proyecto.Fer.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArchivoService archivoService;
    
    
    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(cat -> {


            if (cat.getImagen() != null && !cat.getImagen().isEmpty()) {
                cat.setImagen(archivoService.getImageUrl(cat.getImagen()));
            }
            cat.getSubcategorias().forEach(sub -> {
                if (sub.getImagen() != null && !sub.getImagen().isEmpty()) {
                    sub.setImagen(archivoService.getImageUrl(sub.getImagen()));
                }
            });
            return cat;
        }).collect(Collectors.toList());
    }

    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        if (categoriaOpt.isPresent() && categoriaOpt.get().getImagen() != null && !categoriaOpt.get().getImagen().isEmpty()) {
            Categoria categoria = categoriaOpt.get();
            categoria.setImagen(archivoService.getImageUrl(categoria.getImagen()));
            return Optional.of(categoria);
        }
        return categoriaOpt;
    }
    
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    
    public Categoria guardarCategoriaConImagen(Categoria categoria, MultipartFile imagen) throws IOException {
        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = archivoService.guardarArchivo(imagen);
            categoria.setImagen(nombreArchivo);
        }
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        
        // Asignar URL de la imagen para la respuesta
        if (categoriaGuardada.getImagen() != null && !categoriaGuardada.getImagen().isEmpty()) {
            categoriaGuardada.setImagen(archivoService.getImageUrl(categoriaGuardada.getImagen()));
        }
        
        return categoriaGuardada;
    }
    
   public Categoria actualizarCategoria(Long id, Categoria categoriaNueva) {
    Optional<Categoria> categoriaExistenteOpt = categoriaRepository.findById(id);
    
    if (categoriaExistenteOpt.isPresent()) {
        Categoria categoriaExistente = categoriaExistenteOpt.get();

        // Mantener las subcategorías existentes si no se envían en la actualización
        if (categoriaNueva.getSubcategorias() == null || categoriaNueva.getSubcategorias().isEmpty()) {
            categoriaNueva.setSubcategorias(categoriaExistente.getSubcategorias());
        } else {
            // Asegurar que las subcategorías nuevas tengan la referencia correcta a la categoría
            for (Subcategoria subcategoria : categoriaNueva.getSubcategorias()) {
                subcategoria.setCategoria(categoriaExistente);
            }
        }

        // Mantener la imagen si no se proporciona una nueva
        categoriaNueva.setId(id);
        categoriaNueva.setImagen(categoriaExistente.getImagen());

        // Guardar la categoría actualizada
        Categoria categoriaActualizada = categoriaRepository.save(categoriaNueva);

        // Asignar URL de la imagen para la respuesta
        if (categoriaActualizada.getImagen() != null && !categoriaActualizada.getImagen().isEmpty()) {
            categoriaActualizada.setImagen(archivoService.getImageUrl(categoriaActualizada.getImagen()));
        }

        return categoriaActualizada;
    }
    
    return null;
}
 
    
    public Categoria actualizarCategoriaConImagen(Long id, Categoria categoria, MultipartFile imagen) throws IOException {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        
        if (categoriaExistente.isPresent()) {
            Categoria catActual = categoriaExistente.get();
            
            // Si hay nueva imagen, borrar la anterior y guardar la nueva
            if (imagen != null && !imagen.isEmpty()) {
                if (catActual.getImagen() != null) {
                    archivoService.eliminarArchivo(catActual.getImagen());
                }
                String nombreArchivo = archivoService.guardarArchivo(imagen);
                categoria.setImagen(nombreArchivo);
            } else {
                // Mantener la misma imagen si no se proporciona una nueva
                categoria.setImagen(catActual.getImagen());
            }
            
            categoria.setId(id);
            Categoria categoriaActualizada = categoriaRepository.save(categoria);
            
            // Asignar URL de la imagen para la respuesta
            if (categoriaActualizada.getImagen() != null && !categoriaActualizada.getImagen().isEmpty()) {
                categoriaActualizada.setImagen(archivoService.getImageUrl(categoriaActualizada.getImagen()));
            }
            
            return categoriaActualizada;
        }
        
        return null;
    }
    
    public void eliminarCategoria(Long id) throws IOException {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent() && categoria.get().getImagen() != null) {
            archivoService.eliminarArchivo(categoria.get().getImagen());
        }
        categoriaRepository.deleteById(id);
    }
    
}