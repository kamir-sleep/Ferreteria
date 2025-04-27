package com.proyecto.Fer.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.DTO.VentaDTO;
import com.proyecto.Fer.model.Venta;
import com.proyecto.Fer.repository.VentaRepository;
import com.proyecto.Fer.service.VentaService;
import com.proyecto.Fer.service.Impl.VentaMapper;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository; 
    
    
    @Autowired
    private VentaMapper ventaMapper;

      // Generar código correlativo: VTA-YYYYMMDD-001
    private String generarCodigoVenta() {
        long conteoHoy = ventaRepository.contarVentasHoy() + 1;
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("VTA-%s-%03d", fecha, conteoHoy);
    }

    // 🔎 Listar todas las ventas
@GetMapping
public ResponseEntity<List<Venta>> getAllVentas() {
    List<Venta> ventas = ventaRepository.findAll();
    
    // Crear una nueva lista para no modificar la original
    List<Venta> ventasSerializables = ventas.stream().map(venta -> {
        // Aquí hacemos un fetch completo de todas las propiedades necesarias
        Hibernate.initialize(venta.getDetalles());
        if (venta.getEmpleado() != null) {
            Hibernate.initialize(venta.getEmpleado());
        }
        if (venta.getCliente() != null) {
            Hibernate.initialize(venta.getCliente());
        }
        // Para cada detalle, inicializar la variante
        venta.getDetalles().forEach(detalle -> {
            if (detalle.getVariante() != null) {
                Hibernate.initialize(detalle.getVariante());
            }
        });
        return venta;
    }).collect(Collectors.toList());
    
    return ResponseEntity.ok(ventasSerializables);
}

    // 🔎 Listar todas las ventas con detalles completos (sin DTO)
@GetMapping("/completas")
public ResponseEntity<List<Venta>> listarVentasCompletas() {
    List<Venta> ventas = ventaService.listarVentas();
    return ResponseEntity.ok(ventas);
}

    // 🔎 Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerPorId(id);
        
        // Inicialización para evitar problemas con proxies
        Hibernate.initialize(venta.getDetalles());
        if (venta.getEmpleado() != null) {
            Hibernate.initialize(venta.getEmpleado());
        }
        if (venta.getCliente() != null) {
            Hibernate.initialize(venta.getCliente());
        }
        venta.getDetalles().forEach(detalle -> {
            if (detalle.getVariante() != null) {
                Hibernate.initialize(detalle.getVariante());
            }
        });
        
        return ResponseEntity.ok(venta);
    }

   // 🔎 Buscar por código de venta
   @GetMapping("/codigo/{codigo}")
   public ResponseEntity<Venta> obtenerPorCodigo(@PathVariable String codigo) {
       return ventaRepository.findByCodigoVenta(codigo)
               .map(venta -> ResponseEntity.ok(venta))
               .orElse(ResponseEntity.notFound().build());
   }

    // ➕ Crear nueva venta
    @PostMapping
    @Transactional
    public ResponseEntity<VentaDTO> crear(@RequestBody VentaDTO ventaDTO) {
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta.setCodigoVenta(generarCodigoVenta());
        Venta guardada = ventaService.crearVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaMapper.toDto(guardada));
    }

    // ✏️ Editar venta
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> editar(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        Venta ventaActualizada = ventaMapper.toEntity(ventaDTO);
        Venta editada = ventaService.editarVenta(id, ventaActualizada);
        return ResponseEntity.ok(ventaMapper.toDto(editada));
    }

    // 🗑️ Eliminar lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }

        // Endpoint adicional para obtener datos para PDF
        @GetMapping("/pdf/{id}")
        public ResponseEntity<Venta> obtenerDatosPDF(@PathVariable Long id) {
            Venta venta = ventaService.obtenerPorId(id);
            return ResponseEntity.ok(venta);
        }

}
