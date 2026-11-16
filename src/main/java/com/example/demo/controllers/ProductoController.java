package com.example.demo.controllers;

import com.example.demo.models.Producto;
import com.example.demo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Indica que es una API REST (devuelve JSON, no vistas HTML)
@RestController
// El versionado de la API exigido en la pauta (v1)
@RequestMapping("/api/v1/productos")
// Permite peticiones desde el Frontend de React (puerto 5173/5174)
@CrossOrigin(origins = "*") 
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: /api/v1/productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // GET: /api/v1/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET: /api/v1/productos/categoria/{categoriaId}
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(categoriaId));
    }

    // POST: /api/v1/productos
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT: /api/v1/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.obtenerPorId(id)
                .map(prodExistente -> {
                    // Actualizamos los datos del producto existente
                    prodExistente.setNombre(producto.getNombre());
                    prodExistente.setCodigo(producto.getCodigo());
                    prodExistente.setDescripcion(producto.getDescripcion());
                    prodExistente.setPrecio(producto.getPrecio());
                    prodExistente.setStock(producto.getStock());
                    prodExistente.setStockCritico(producto.getStockCritico());
                    prodExistente.setImagenUrl(producto.getImagenUrl());
                    prodExistente.setCategoria(producto.getCategoria());
                    
                    return ResponseEntity.ok(productoService.guardarProducto(prodExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: /api/v1/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.obtenerPorId(id).isPresent()) {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}