
package com.proyecto.Fer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.Fer.DTO.CambioPasswordDTO;
import com.proyecto.Fer.DTO.ClienteDTO;
import com.proyecto.Fer.model.Cliente;
import com.proyecto.Fer.service.ClienteService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clientes")
public class ClienteController {

     @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<ClienteDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO cliente) {
        ClienteDTO createdCliente = clienteService.save(cliente);
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }

    @PostMapping("/registro")
    public ResponseEntity<ClienteDTO> registroClienteConUsuario(@Valid @RequestBody ClienteDTO cliente) {
        ClienteDTO createdCliente = clienteService.registroClienteConUsuario(cliente);
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }

    @GetMapping("/perfil")
    public ResponseEntity<ClienteDTO> obtenerPerfilCliente() {
        ClienteDTO clienteActual = clienteService.obtenerClienteLogueado();
        return ResponseEntity.ok(clienteActual);
    }


    
    @PutMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(@Valid @RequestBody CambioPasswordDTO cambioPasswordDTO) {
        clienteService.cambiarPassword(cambioPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
        @PathVariable Long id, 
        @Valid @RequestBody ClienteDTO clienteDetails
    ) {
        ClienteDTO updatedCliente = clienteService.update(id, clienteDetails);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

