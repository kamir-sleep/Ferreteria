package com.proyecto.Fer.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.model.Empleado;
import com.proyecto.Fer.service.EmpleadoService;

// Controlador
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        try {
            Empleado empleado = empleadoService.obtenerPorId(id);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public Empleado crear(@RequestBody Empleado empleado) {
        return empleadoService.crearOActualizar(empleado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado empleadoDetalles) {
        try {
            Empleado empleadoExistente = empleadoService.obtenerPorId(id);
            empleadoDetalles.setId(id);
            return ResponseEntity.ok(empleadoService.crearOActualizar(empleadoDetalles));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            empleadoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/calcular-vacaciones")
    public ResponseEntity<Void> calcularVacaciones(@PathVariable Long id, @RequestParam LocalDate fechaActual) {
        try {
            empleadoService.calcularVacaciones(id, fechaActual);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
