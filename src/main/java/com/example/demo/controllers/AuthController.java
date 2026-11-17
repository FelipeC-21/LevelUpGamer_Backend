package com.example.demo.controllers;

import com.example.demo.models.Usuario;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String password = credenciales.get("password");

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorCorreo(correo);

        if (usuarioOpt.isPresent() && passwordEncoder.matches(password, usuarioOpt.get().getPassword())) {
            Usuario usuario = usuarioOpt.get();
            // Generar Token JWT con el rol incluido
            String token = jwtUtils.generarToken(usuario.getCorreo(), usuario.getRol());
            
            return ResponseEntity.ok(Map.of("token", token, "rol", usuario.getRol()));
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}