package com.proyecto.Fer.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    private String apellidoMaterno;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "genero", length = 10)
    private String genero;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado; // Activo, inactivo, suspendido

    @Column(name = "celular", length = 15)
    private String celular;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "descanso_inicio")
    private LocalTime descansoInicio;

    @Column(name = "descanso_fin")
    private LocalTime descansoFin;

    @Column(name = "horas_trabajadas_diarias")
    private double horasTrabajadasDiarias;

    @Column(name = "horas_extras")
    private double horasExtras;

    @Column(name = "tasa_por_hora")
    private double tasaPorHora;

    @Column(name = "salario_base")
    private double salarioBase;

    @Column(name = "salario_por_horas_extras")
    private double salarioPorHorasExtras;

    @Column(name = "salario_total")
    private double salarioTotal;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "dias_vacaciones")
    private int diasVacaciones;

    @Column(name = "banco", length = 100)
    private String banco;

    @Column(name = "numero_cuenta", length = 20)
    private String numeroCuenta;

    @Column(name = "ultima_actualizacion_salarial")
    private LocalDate ultimaActualizacionSalarial;


    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "usuario_login", referencedColumnName = "login")
    private UsuariosModel usuario;

    // Getters y setters omitidos por brevedad

    // Método para calcular las horas trabajadas diarias
    public void calcularHorasTrabajadasDiarias() {
        if (horaEntrada == null || horaSalida == null || descansoInicio == null || descansoFin == null) {
            throw new IllegalArgumentException("Los valores de horaEntrada, horaSalida, descansoInicio y descansoFin no pueden ser nulos.");
        }

        long horasTrabajadas = java.time.Duration.between(horaEntrada, horaSalida).toHours();
        long horasDescanso = java.time.Duration.between(descansoInicio, descansoFin).toHours();
        this.horasTrabajadasDiarias = horasTrabajadas - horasDescanso;
    }

    // Método para calcular el salario total
    public void calcularSalarioTotal() {
        this.salarioPorHorasExtras = this.horasExtras * this.tasaPorHora * 1.5;
        this.salarioTotal = (this.horasTrabajadasDiarias * this.tasaPorHora) + this.salarioPorHorasExtras;
    }

    // Método para calcular días de vacaciones acumulados
    public void calcularDiasVacaciones(LocalDate fechaActual) {
        long antiguedadEnDias = java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, fechaActual);
        this.diasVacaciones = (int) (antiguedadEnDias / 365) * 12 / 12; // Por ejemplo: 12 días al año
    }


}
