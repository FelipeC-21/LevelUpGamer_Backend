package com.example.demo.config;

import com.example.demo.models.Categoria;
import com.example.demo.models.Producto;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.CategoriaRepository;
import com.example.demo.repositories.ProductoRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        cargarUsuariosIniciales();
        cargarCatalogoInicial();
    }

    private void cargarUsuariosIniciales() {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setRut("123456785");
            admin.setNombreCompleto("Admin Principal");
            admin.setCorreo("admin@duoc.cl");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("Administrador");
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setRegion("Región Metropolitana");
            admin.setComuna("Santiago");
            admin.setDireccion("Av. Siempre Viva 123");
            usuarioRepository.save(admin);

            Usuario vendedor = new Usuario();
            vendedor.setRut("111111111");
            vendedor.setNombreCompleto("Vendedor Prueba");
            vendedor.setCorreo("vendedor@duoc.cl");
            vendedor.setPassword(passwordEncoder.encode("vend123"));
            vendedor.setRol("Vendedor");
            vendedor.setFechaNacimiento(LocalDate.of(1995, 5, 15));
            vendedor.setRegion("Valparaíso");
            vendedor.setComuna("Viña del Mar");
            vendedor.setDireccion("Calle Falsa 456");
            usuarioRepository.save(vendedor);

            Usuario cliente = new Usuario();
            cliente.setRut("19011022K");
            cliente.setNombreCompleto("Camila Rojas");
            cliente.setCorreo("cliente@gmail.com");
            cliente.setPassword(passwordEncoder.encode("clie123"));
            cliente.setRol("Cliente");
            cliente.setFechaNacimiento(LocalDate.of(2000, 8, 20));
            cliente.setRegion("Región Metropolitana");
            cliente.setComuna("Maipú");
            cliente.setDireccion("Av. Pajaritos 742");
            usuarioRepository.save(cliente);

            System.out.println("Usuarios iniciales cargados (admin@duoc.cl, vendedor@duoc.cl, cliente@gmail.com)");
        }
    }

    private void cargarCatalogoInicial() {
        if (categoriaRepository.count() == 0) {
            Categoria cJuegos = crearCategoria("Juegos de Mesa", "Estrategia y diversión grupal");
            Categoria cConsolas = crearCategoria("Consolas", "Consolas de última generación");
            Categoria cAccesorios = crearCategoria("Accesorios", "Audio y periféricos gamer");
            Categoria cComputadores = crearCategoria("Computadores Gamers", "PCs y notebooks de alto rendimiento");
            Categoria cSillas = crearCategoria("Sillas Gamers", "Ergonomía para largas sesiones");
            Categoria cMouse = crearCategoria("Mouse", "Precisión óptica para gaming");
            Categoria cMousepad = crearCategoria("Mousepad", "Superficies deslizantes extendidas");
            Categoria cRopa = crearCategoria("Poleras y Polerones", "Merchandising oficial Level-Up");

            if (productoRepository.count() == 0) {
                crearProducto("JDM-001", "Catan", "Juego de estrategia multijugador", 45000, 15, 5, cJuegos);
                crearProducto("JDM-002", "Carcassonne", "Juego de mesa clásico de fichas", 35000, 10, 3, cJuegos);
                crearProducto("CNS-001", "Xbox Series X", "Consola de Microsoft de última generación", 550000, 8, 2, cConsolas);
                crearProducto("ACC-001", "HyperX Cloud II", "Audífonos gaming con sonido 7.1", 80000, 2, 5, cAccesorios);
                crearProducto("CNS-002", "PlayStation 5", "PlayStation 5 de Sony", 600000, 5, 3, cConsolas);
                crearProducto("PCG-001", "ASUS ROG Strix", "PC Gamer de alto rendimiento", 1200000, 3, 1, cComputadores);
                crearProducto("SLL-001", "Secretlab Titan", "Silla ergonómica premium", 450000, 0, 2, cSillas);
                crearProducto("MOU-001", "Logitech G502", "Mouse gaming con peso ajustable", 50000, 20, 5, cMouse);
                crearProducto("MPD-001", "Razer Goliathus", "Mousepad extendido antideslizante", 25000, 30, 10, cMousepad);
                crearProducto("POL-001", "Polera Level-Up", "Polera promocional oficial", 0, 50, 10, cRopa);
                System.out.println("Catálogo inicial de 8 categorías y 10 productos cargado en MySQL");
            }
        }
    }

    private Categoria crearCategoria(String nombre, String descripcion) {
        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        cat.setDescripcion(descripcion);
        return categoriaRepository.save(cat);
    }

    private void crearProducto(String codigo, String nombre, String desc, int precio, int stock, int critico, Categoria cat) {
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setDescripcion(desc);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setStockCritico(critico);
        p.setImagenUrl("");
        p.setCategoria(cat);
        productoRepository.save(p);
    }
}