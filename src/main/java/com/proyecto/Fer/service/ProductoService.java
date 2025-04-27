package com.proyecto.Fer.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.Fer.config.FileStorageService;
import com.proyecto.Fer.model.Marca;
import com.proyecto.Fer.model.Producto;
import com.proyecto.Fer.model.Subcategoria;
import com.proyecto.Fer.repository.MarcaRepository;
import com.proyecto.Fer.repository.ProductoRepository;
import com.proyecto.Fer.repository.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final MarcaRepository marcaRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProductoService(
            ProductoRepository productoRepository,
            SubcategoriaRepository subcategoriaRepository,
            MarcaRepository marcaRepository,
            FileStorageService fileStorageService) {
        this.productoRepository = productoRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.marcaRepository = marcaRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
    }

    public List<Producto> getProductosBySubcategoria(Long subcategoriaId) {
        return productoRepository.findBySubcategoriaId(subcategoriaId);
    }

    public List<Producto> getProductosByMarca(Long marcaId) {
        return productoRepository.findByMarcaId(marcaId);
    }


    public Producto createProducto(Producto producto, Long subcategoriaId, Long marcaId, MultipartFile imagen) throws IOException {
        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategoría no encontrada con id: " + subcategoriaId));
        
        // Asignar marca si se proporciona
        if (marcaId != null) {
            Marca marca = marcaRepository.findById(marcaId)
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con id: " + marcaId));
            producto.setMarca(marca);
        }
        
        producto.setSubcategoria(subcategoria);
        
        // Manejar la imagen si se proporciona
        if (imagen != null && !imagen.isEmpty()) {
            String fileName = fileStorageService.storeFile(imagen);
            producto.setImagen(fileName);
        }
        
        return productoRepository.save(producto);
    }

    public Producto updateProducto(Long id, Producto productoDetails, Long subcategoriaId, Long marcaId, MultipartFile imagen) throws IOException {
        Producto producto = getProductoById(id);
        
        // Actualizar campos básicos
        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setEstado(productoDetails.getEstado());
        producto.setTipoMedia(productoDetails.getTipoMedia());
        
        // Actualizar subcategoría si se proporciona
        if (subcategoriaId != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                    .orElseThrow(() -> new EntityNotFoundException("Subcategoría no encontrada con id: " + subcategoriaId));
            producto.setSubcategoria(subcategoria);
        }
        
        // Actualizar marca si se proporciona
        if (marcaId != null) {
            Marca marca = marcaRepository.findById(marcaId)
                    .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con id: " + marcaId));
            producto.setMarca(marca);
        }
        
        // Actualizar imagen si se proporciona
        if (imagen != null && !imagen.isEmpty()) {
            // Eliminar imagen anterior si existe
            if (producto.getImagen() != null) {
                fileStorageService.deleteFile(producto.getImagen());
            }
            String fileName = fileStorageService.storeFile(imagen);
            producto.setImagen(fileName);
        }
        
        return productoRepository.save(producto);
    }

    public void deleteProducto(Long id) throws IOException {
        Producto producto = getProductoById(id);
        
        // Eliminar imagen si existe
        if (producto.getImagen() != null) {
            fileStorageService.deleteFile(producto.getImagen());
        }
        
        productoRepository.delete(producto);
    }
}
