package com.s8mil.bank_webapp.controller;

import org.springframework.security.core.Authentication;
import com.s8mil.bank_webapp.dto.CuentaDto;
import com.s8mil.bank_webapp.dto.DepositRequestDto;
import com.s8mil.bank_webapp.dto.WithdrawRequestDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final RepositorioClientes clienteRepository;

    public CuentaController(CuentaService cuentaService, RepositorioClientes clienteRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaService = cuentaService;
    }

    // REST API CREAR CUENTA
    @PostMapping
    public ResponseEntity<CuentaDto> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        CuentaDto nuevaCuenta = cuentaService.crearCuenta(cuentaDto);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    // REST API CUENTA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDto> obtenerCuenta(@PathVariable Long id) {
        CuentaDto cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    //MUESTRA LAS CUENTAS
    @GetMapping("/mis-cuentas")
    public ResponseEntity<List<CuentaDto>> obtenerCuentasDelCliente(Authentication authentication) {
        String email = authentication.getName(); // del token JWT
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<CuentaDto> cuentas = cuentaService.obtenerCuentasPorClienteId(cliente.getId());
        return ResponseEntity.ok(cuentas);
    }

    // REST API TODAS LAS CUENTAS
    @GetMapping
    public ResponseEntity<List<CuentaDto>> listarCuentas() {
        List<CuentaDto> cuentas = cuentaService.getAllCuentas();
        return ResponseEntity.ok(cuentas);
    }

    // REST API ELIMINAR CUENTA
    @DeleteMapping("/{clienteId}/cuentas/{cuentaId}")
    public ResponseEntity<?> eliminarCuenta(
            @PathVariable Long clienteId,
            @PathVariable Long cuentaId) {
        try {
            cuentaService.eliminarCuenta(clienteId, cuentaId);
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //REST API DEPOSITAR
    @PutMapping("/{id}/depositar")
    public ResponseEntity<CuentaDto> depositar(@PathVariable Long id, @RequestBody DepositRequestDto request) {
        CuentaDto cuentaActualizada = cuentaService.deposit(id, request.getMonto());
        return ResponseEntity.ok(cuentaActualizada);
    }

    //REST API RETIRAR
    @PutMapping("/{id}/retirar")
    public ResponseEntity<CuentaDto> retirar(@PathVariable Long id, @RequestBody WithdrawRequestDto request) {
        CuentaDto cuentaActualizada = cuentaService.withdraw(id, request.getMonto());
        return ResponseEntity.ok(cuentaActualizada);
    }
}
