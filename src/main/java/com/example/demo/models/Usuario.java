package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data // Lombok: Genera Getters, Setters y toString automáticamente
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false)
    private String password;

    // Roles permitidos: "Administrador", "Vendedor", "Cliente"
    @Column(nullable = false, length = 20)
    private String rol;

    private LocalDate fechaNacimiento;
    private String region;
    private String comuna;
    private String direccion;

    // Relación: Un usuario puede tener muchas boletas (compras)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Boleta> boletas;
}