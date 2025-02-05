package com.proyecto.Fer.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.Fer.model.Empleado;
import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.repository.EmpleadoRepository;
import com.proyecto.Fer.repository.UsuariosRepo;
import com.proyecto.Fer.DTO.CambioPasswordDTO;
import com.proyecto.Fer.exception.ResourceNotFoundException;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuariosRepo usuariosRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos() {
        return empleadoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + id));
    }

    @Transactional
    public Empleado crearEmpleado(Empleado empleado) {
        if (usuariosRepo.existsByLogin(empleado.getEmail())) {
            throw new RuntimeException("El email ya está registrado como usuario");
        }

        UsuariosModel usuario = new UsuariosModel();
        usuario.setLogin(empleado.getEmail());
        usuario.setPasswd(passwordEncoder.encode(empleado.getCelular())); // Celular como contraseña
        usuario.setEstado(1);

        empleado.setUsuario(usuario);

        return empleadoRepository.save(empleado);
    }

    @Transactional
    public Empleado actualizarEmpleado(Long id, Empleado empleadoDetalles) {
        Empleado empleadoExistente = obtenerPorId(id);

        empleadoExistente.setNombre(empleadoDetalles.getNombre());
        empleadoExistente.setApellidoPaterno(empleadoDetalles.getApellidoPaterno());
        empleadoExistente.setApellidoMaterno(empleadoDetalles.getApellidoMaterno());
        empleadoExistente.setDireccion(empleadoDetalles.getDireccion());
        empleadoExistente.setGenero(empleadoDetalles.getGenero());
        empleadoExistente.setCiudad(empleadoDetalles.getCiudad());
        empleadoExistente.setEstado(empleadoDetalles.getEstado());
        empleadoExistente.setCelular(empleadoDetalles.getCelular());
        empleadoExistente.setFechaIngreso(empleadoDetalles.getFechaIngreso());

        return empleadoRepository.save(empleadoExistente);
    }

    @Transactional
    public void eliminarEmpleado(Long id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginActual = authentication.getName();

        return empleadoRepository.findByUsuario_Login(loginActual)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
    }

    @Transactional
    public void cambiarPassword(CambioPasswordDTO cambioPasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginActual = authentication.getName();

        UsuariosModel usuario = usuariosRepo.findByLogin(loginActual)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(cambioPasswordDTO.getPasswordActual(), usuario.getPasswd())) {
            throw new ResourceNotFoundException("Contraseña actual incorrecta");
        }

        usuario.setPasswd(passwordEncoder.encode(cambioPasswordDTO.getNuevaPassword()));
        usuariosRepo.save(usuario);
    }

    @Transactional
    public void calcularVacaciones(Long id, LocalDate fechaActual) {
        Empleado empleado = obtenerPorId(id);
        empleado.calcularDiasVacaciones(fechaActual);
        empleadoRepository.save(empleado);
    }
}
