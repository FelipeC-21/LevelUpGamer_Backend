package com.example.demo.controllers;

import com.example.demo.models.Usuario;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            usuario.setPassword("levelup123"); // Clave por defecto al crear desde admin
        }
        return new ResponseEntity<>(usuarioService.guardarUsuario(usuario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datos) {
        return usuarioService.obtenerPorId(id)
                .map(existente -> {
                    existente.setNombreCompleto(datos.getNombreCompleto());
                    existente.setCorreo(datos.getCorreo());
                    existente.setRol(datos.getRol());
                    existente.setFechaNacimiento(datos.getFechaNacimiento());
                    existente.setRegion(datos.getRegion());
                    existente.setComuna(datos.getComuna());
                    existente.setDireccion(datos.getDireccion());
                    if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                        existente.setPassword(datos.getPassword());
                    }
                    return ResponseEntity.ok(usuarioService.guardarUsuario(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioService.obtenerPorId(id).isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}