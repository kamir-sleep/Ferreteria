package com.proyecto.Fer.service;



import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Fer.model.Empleado;
import com.proyecto.Fer.repository.EmpleadoRepository;

// Servicio
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
    }

    public Empleado crearOActualizar(Empleado empleado) {
        empleado.calcularHorasTrabajadasDiarias();
        empleado.calcularSalarioTotal();
        return empleadoRepository.save(empleado);
    }

    public void eliminar(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Empleado con ID: " + id + " no existe.");
        }
        empleadoRepository.deleteById(id);
    }

    public void calcularVacaciones(Long id, LocalDate fechaActual) {
        Empleado empleado = obtenerPorId(id);
        empleado.calcularDiasVacaciones(fechaActual);
        empleadoRepository.save(empleado);
    }
}