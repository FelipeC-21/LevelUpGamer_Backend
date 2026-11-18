package com.example.demo.controllers;

import com.example.demo.models.Usuario;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            String token = jwtUtils.generarToken(usuario.getCorreo(), usuario.getRol());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "id", usuario.getId(),
                    "nombreCompleto", usuario.getNombreCompleto(),
                    "correo", usuario.getCorreo(),
                    "rol", usuario.getRol()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales incorrectas"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario nuevoUsuario) {
        if (usuarioService.obtenerPorCorreo(nuevoUsuario.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El correo ya se encuentra registrado"));
        }
        // Todo registro público entra por defecto con rol Cliente
        nuevoUsuario.setRol("Cliente");
        Usuario guardado = usuarioService.guardarUsuario(nuevoUsuario);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }
}