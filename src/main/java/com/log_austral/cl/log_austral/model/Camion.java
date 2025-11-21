package com.log_austral.cl.log_austral.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false)
    private String patente;
    
    @Column(nullable = true)
    private String marca;
    
    @Column(nullable = true)
    private String modelo;
    
    @Column(nullable = true)
    private Integer annio;
    
    @Column(nullable = true)
    private String tipo;
    
    @Column(nullable = true)
    private Integer capacidad;
    
    @Column(nullable = true)
    private Boolean disponibilidad;
    
    @Column(nullable = true)
    private String estado;
    
    @Column(nullable = true)
    private String descripcion;
    
    @Column(nullable = true)
    private String traccion;
    
    @Column(nullable = true)
    private Integer precio;
    
    @Column(nullable = true)
    private String imagenUri; // string para URI
}
