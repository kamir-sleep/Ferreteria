package com.proyecto.Fer.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "documento_identidad", unique = true)
    private String documentoIdentidad;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "genero")
    private String genero;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "estado")
    private int estado;

    @Column(name = "nit")
    private String nit;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_login", referencedColumnName = "login")
    private UsuariosModel usuario;


   @PrePersist
    public void prePersist() {
        // Automatically set fecha_registro to current date when creating
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDate.now();
        }
        // Ensure estado is set to 1 (active) when creating
        this.estado = 1;
    }
}
 