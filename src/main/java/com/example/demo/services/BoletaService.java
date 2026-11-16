package com.example.demo.services;

import com.example.demo.models.Boleta;
import com.example.demo.models.DetalleBoleta;
import com.example.demo.models.Producto;
import com.example.demo.repositories.BoletaRepository;
import com.example.demo.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Boleta> obtenerTodas() {
        return boletaRepository.findAll();
    }

    public List<Boleta> obtenerPorUsuario(Long usuarioId) {
        return boletaRepository.findByUsuarioId(usuarioId);
    }

    // @Transactional asegura que si ocurre un error (ej. falta de stock), 
    // no se guarde ni la boleta ni se descuente stock a medias.
    @Transactional
    public Boleta procesarCompra(Boleta boleta) {
        boleta.setFechaCompra(LocalDateTime.now());
        
        // Generar un número de orden único automáticamente si no viene uno
        if (boleta.getNroOrden() == null || boleta.getNroOrden().isEmpty()) {
            boleta.setNroOrden("ORDER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        boleta.setEstado("Pagado");

        int totalCalculado = 0;

        // Lógica de negocio: Verificar stock y descontar inventario
        for (DetalleBoleta detalle : boleta.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado en la base de datos"));

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Descontar stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto); // Actualiza el producto en la BD

            // Enlazar bidireccionalmente el detalle con la boleta para evitar errores de llave foránea
            detalle.setBoleta(boleta);
            
            // Recalcular subtotal por seguridad (evita que el frontend envíe precios alterados)
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            totalCalculado += detalle.getSubtotal();
        }

        boleta.setTotalPagado(totalCalculado);

        // Guardar la boleta en cascada (guarda la boleta y sus detalles automáticamente)
        return boletaRepository.save(boleta);
    }
}