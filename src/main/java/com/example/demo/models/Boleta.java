package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boletas")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nroOrden;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private Integer totalPagado;

    private String estado; // Ej: "Pagado", "Rechazado", "Enviado"

    // Relación: Muchas boletas pertenecen a un usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación: Una boleta tiene muchos detalles (ítems comprados)
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL)
    private List<DetalleBoleta> detalles;
}