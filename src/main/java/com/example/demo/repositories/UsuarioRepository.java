package com.example.demo.repositories;

import com.example.demo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Permite buscar un usuario por su RUT
    Optional<Usuario> findByRut(String rut);
    
    // Permite buscar un usuario por su correo (útil para Login/JWT)
    Optional<Usuario> findByCorreo(String correo);
}