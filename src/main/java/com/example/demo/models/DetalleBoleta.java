package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalles_boleta")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer precioUnitario;

    @Column(nullable = false)
    private Integer subtotal;

    // Relación: Muchos detalles pertenecen a una boleta
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "boleta_id", nullable = false)
    private Boleta boleta;

    // Relación: Un detalle corresponde a un producto específico
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}