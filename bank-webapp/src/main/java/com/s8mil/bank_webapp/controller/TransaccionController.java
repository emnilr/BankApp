package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.TransaccionDto;
import com.s8mil.bank_webapp.dto.TransferenciaRequestDto;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Transaccion;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.repository.RepositorioCuentas;
import com.s8mil.bank_webapp.repository.RepositorioTransacciones;
import com.s8mil.bank_webapp.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final RepositorioCuentas repositorioCuentas;
    private final RepositorioClientes repositorioClientes;
    private final RepositorioTransacciones repositorioTransacciones;

    public TransaccionController(TransaccionService transaccionService, RepositorioCuentas repositorioCuentas, RepositorioClientes repositorioClientes, RepositorioTransacciones repositorioTransacciones) {
        this.transaccionService = transaccionService;
        this.repositorioCuentas = repositorioCuentas;
        this.repositorioClientes = repositorioClientes;
        this.repositorioTransacciones = repositorioTransacciones;
    }

    @PostMapping
    public ResponseEntity<TransaccionDto> crearTransaccion(@RequestBody TransaccionDto dto) {
        TransaccionDto creada = transaccionService.crearTransaccion(dto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    // REST API TODAS LAS TRANSACCIONES
    @GetMapping
    public ResponseEntity<List<TransaccionDto>> obtenerTodasLasTransacciones() {
        return ResponseEntity.ok(transaccionService.getAllTransacciones());
    }

    // REST API TRANSACCIONES POR CUENTA
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<TransaccionDto>> obtenerTransaccionesPorCuenta(@PathVariable Long cuentaId) {
        return ResponseEntity.ok(transaccionService.getTransaccionesPorCuenta(cuentaId));
    }

    // REST API TRANSACCIONES POR CLIENTE
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<TransaccionDto>> obtenerTransaccionesPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(transaccionService.getTransaccionesPorCliente(clienteId));
    }

    //TRANSFERIR ENTRE CLIENTES
    @PostMapping("/transferir")
    public ResponseEntity<Map<String, String>> transferir(@RequestBody TransferenciaRequestDto request) {

        Long cuentaOrigenId = request.getCuentaOrigenId();
        String numeroCuentaDestinatario = request.getNumeroCuentaDestinatario();
        double monto = request.getMonto();

        Cuenta cuentaOrigen = repositorioCuentas.findById(cuentaOrigenId)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));

        if (cuentaOrigen.getSaldo() < monto) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fondos insuficientes"));
        }

        Cuenta cuentaDestino = repositorioCuentas.findByNumeroCuenta(numeroCuentaDestinatario)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));

        // Realizar transferencia
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);

        repositorioCuentas.save(cuentaOrigen);
        repositorioCuentas.save(cuentaDestino);

        // Registrar transacciones
        // Crear transacción de salida
        Transaccion transaccionSalida = new Transaccion();
        transaccionSalida.setCuenta(cuentaOrigen);
        transaccionSalida.setMonto(monto);
        transaccionSalida.setTipo("TRANSFERENCIA_SALIDA");
        transaccionSalida.setFecha(LocalDateTime.now());
        repositorioTransacciones.save(transaccionSalida);

// Crear transacción de entrada
        Transaccion transaccionEntrada = new Transaccion();
        transaccionEntrada.setCuenta(cuentaDestino);
        transaccionEntrada.setMonto(monto);
        transaccionEntrada.setTipo("TRANSFERENCIA_ENTRADA");
        transaccionEntrada.setFecha(LocalDateTime.now());
        repositorioTransacciones.save(transaccionEntrada);

// Relación entre transacciones
        transaccionSalida.setTransaccionRelacionada(transaccionEntrada);
        transaccionEntrada.setTransaccionRelacionada(transaccionSalida);

// Guardar ambas
        repositorioTransacciones.save(transaccionSalida);
        repositorioTransacciones.save(transaccionEntrada);


        // Retornar info útil
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Transferencia realizada exitosamente");
        respuesta.put("numeroCuentaOrigen", cuentaOrigen.getNumeroCuenta());
        respuesta.put("numeroCuentaDestino", cuentaDestino.getNumeroCuenta());

        return ResponseEntity.ok(respuesta);
    }
}
