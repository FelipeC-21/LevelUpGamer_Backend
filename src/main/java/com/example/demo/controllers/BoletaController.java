package com.example.demo.controllers;

import com.example.demo.models.Boleta;
import com.example.demo.services.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
@CrossOrigin(origins = "*")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @GetMapping
    public ResponseEntity<List<Boleta>> listarTodas() {
        return ResponseEntity.ok(boletaService.obtenerTodas());
    }

    // El frontend enviará el JSON del carrito a este endpoint
    @PostMapping
    public ResponseEntity<?> procesarCompra(@RequestBody Boleta boleta) {
        try {
            Boleta nuevaBoleta = boletaService.procesarCompra(boleta);
            return new ResponseEntity<>(nuevaBoleta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Devuelve error 400 si falta stock o hay un problema transaccional
            return ResponseEntity.badRequest().body("Error al procesar la compra: " + e.getMessage());
        }
    }
}