package com.example.demo;

import com.example.demo.models.Producto;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.ProductoRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.ProductoService;
import com.example.demo.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LevelUpBackendTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UsuarioService usuarioService;

    @InjectMocks
    private ProductoService productoService;

    private final JwtUtils jwtUtils = new JwtUtils();

    // TEST 1: Generación y validación de Token JWT válido
    @Test
    void debeGenerarYValidarTokenJwtCorrectamente() {
        String token = jwtUtils.generarToken("admin@duoc.cl", "Administrador");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(jwtUtils.validarToken(token));
        assertEquals("admin@duoc.cl", jwtUtils.extraerCorreo(token));
    }

    // TEST 2: Rechazo de Token JWT alterado o malformado
    @Test
    void debeRechazarTokenJwtInvalido() {
        boolean esValido = jwtUtils.validarToken("token.falso.alterado");

        assertFalse(esValido);
    }

    // TEST 3: Carga de usuario existente y asignación de autoridad ROLE_ADMINISTRADOR
    @Test
    void debeCargarUserDetailsConRolCorrecto() {
        Usuario admin = new Usuario();
        admin.setCorreo("admin@duoc.cl");
        admin.setPassword("$2a$10$hashSimulado");
        admin.setRol("Administrador");

        when(usuarioRepository.findByCorreo("admin@duoc.cl")).thenReturn(Optional.of(admin));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin@duoc.cl");

        assertEquals("admin@duoc.cl", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR")));
    }

    // TEST 4: Excepción controlada al intentar autenticar un correo inexistente
    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioRepository.findByCorreo("noexiste@duoc.cl")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("noexiste@duoc.cl");
        });
    }

    // TEST 5: Encriptación automática con BCrypt al registrar un usuario nuevo
    @Test
    void debeEncriptarPasswordConBcryptAlGuardarUsuario() {
        Usuario nuevo = new Usuario();
        nuevo.setCorreo("cliente@duoc.cl");
        nuevo.setPassword("clave123");

        when(passwordEncoder.encode("clave123")).thenReturn("$2a$10$hashSeguroBcrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario guardado = usuarioService.guardarUsuario(nuevo);

        assertEquals("$2a$10$hashSeguroBcrypt", guardado.getPassword());
        verify(passwordEncoder, times(1)).encode("clave123");
        verify(usuarioRepository, times(1)).save(nuevo);
    }

    // TEST 6: Evitar doble encriptación si la contraseña ya está hasheada en BCrypt
    @Test
    void noDebeReEncriptarPasswordYaHasheada() {
        Usuario existente = new Usuario();
        existente.setCorreo("vendedor@duoc.cl");
        existente.setPassword("$2a$10$hashYaGuardado");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario actualizado = usuarioService.guardarUsuario(existente);

        assertEquals("$2a$10$hashYaGuardado", actualizado.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    // TEST 7: Consulta del listado de productos desde el servicio
    @Test
    void debeRetornarListadoDeProductosDesdeRepositorio() {
        Producto p1 = new Producto();
        p1.setCodigo("JDM-001");
        p1.setNombre("Catan");
        p1.setPrecio(45000);
        p1.setStock(15);

        Producto p2 = new Producto();
        p2.setCodigo("CNS-002");
        p2.setNombre("PlayStation 5");
        p2.setPrecio(600000);
        p2.setStock(5);

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Catan", resultado.get(0).getNombre());
        assertEquals("PlayStation 5", resultado.get(1).getNombre());
    }
}