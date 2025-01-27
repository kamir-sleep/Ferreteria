package com.proyecto.Fer.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Fer.model.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Optional: Add custom query methods if needed
    Cliente findByDocumentoIdentidad(String documentoIdentidad);
    boolean existsByDocumentoIdentidad(String documentoIdentidad);
    Optional<Cliente> findByUsuario_Login(String login);
}