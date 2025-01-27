package com.proyecto.Fer.service;



import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import org.springframework.security.core.Authentication;
import com.proyecto.Fer.DTO.CambioPasswordDTO;
import com.proyecto.Fer.DTO.ClienteDTO;
import com.proyecto.Fer.exception.ResourceNotFoundException;
import com.proyecto.Fer.model.Cliente;
import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.repository.ClienteRepository;
import com.proyecto.Fer.repository.UsuariosRepo;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

      @Autowired
    private UsuariosRepo usuariosRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return convertToDto(cliente);
    }

    @Transactional
    public ClienteDTO save(@Valid ClienteDTO clienteDTO) {
        // Validate unique document
        if (clienteRepository.existsByDocumentoIdentidad(clienteDTO.getDocumentoIdentidad())) {
            throw new ResourceNotFoundException("Ya existe un cliente con este documento de identidad");
        }
        
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDto(savedCliente);
    }

    @Transactional
    public ClienteDTO update(Long id, @Valid ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        // Preserve the original document identity
        clienteDTO.setDocumentoIdentidad(existingCliente.getDocumentoIdentidad());
        
        BeanUtils.copyProperties(clienteDTO, existingCliente, "idCliente", "fechaRegistro", "documentoIdentidad");
        
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return convertToDto(updatedCliente);
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        clienteRepository.delete(cliente);
    }

    // Conversion methods
    private ClienteDTO convertToDto(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        BeanUtils.copyProperties(cliente, dto);
        return dto;
    }

    private Cliente convertToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(dto, cliente);
        return cliente;
    }



     @Transactional
    public ClienteDTO registroClienteConUsuario(ClienteDTO clienteDTO) {
        // Validate unique document
        if (clienteRepository.existsByDocumentoIdentidad(clienteDTO.getDocumentoIdentidad())) {
            throw new ResourceNotFoundException("Ya existe un cliente con este documento de identidad");
        }
        
        // Convert DTO to entity
        Cliente cliente = convertToEntity(clienteDTO);
        
        // Create user with email as login and phone as initial password
        UsuariosModel usuario = new UsuariosModel();
        usuario.setLogin(cliente.getEmail());
        usuario.setPasswd(passwordEncoder.encode(cliente.getTelefono()));
        usuario.setEstado(1);
        
        // Set user for client
        cliente.setUsuario(usuario);
        
        // Save client (which will cascade save the user)
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDto(savedCliente);
    }


   public ClienteDTO obtenerClienteLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginActual = authentication.getName();
        
        Cliente cliente = clienteRepository.findByUsuario_Login(loginActual)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        
        return convertToDto(cliente);
    }
    @Transactional
    public void cambiarPassword(CambioPasswordDTO cambioPasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginActual = authentication.getName();
        
        // Verify current user
        UsuariosModel usuario = usuariosRepo.findByLogin(loginActual)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Verify current password
        if (!passwordEncoder.matches(cambioPasswordDTO.getPasswordActual(), usuario.getPasswd())) {
            throw new ResourceNotFoundException("Contraseña actual incorrecta");
        }
        
        // Encode and save new password
        String nuevaPasswordEncriptada = passwordEncoder.encode(cambioPasswordDTO.getNuevaPassword());
        usuariosRepo.cambiarPassword(loginActual, nuevaPasswordEncriptada);
    }


}