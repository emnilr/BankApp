package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.*;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.service.ClienteService;
import com.s8mil.bank_webapp.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    private final CuentaService cuentaService;
    private final RepositorioClientes clienteRepository;

    public ClienteController(ClienteService clienteService, CuentaService cuentaService, RepositorioClientes clienteRepository) {
        this.clienteService = clienteService;
        this.cuentaService = cuentaService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(@RequestBody ClienteDto clienteDto) {
        ClienteDto nuevoCliente = clienteService.crearCliente(clienteDto);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerCliente(@PathVariable Long id) {
        ClienteDto cliente = clienteService.getClientbyId(id);
        return ResponseEntity.ok(cliente);
    }


    //REST API TODOS LOS CLIENTES
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes() {
        List<ClienteDto> clientes = clienteService.getAllClientes();
        return ResponseEntity.ok(clientes);
    }

    //REST API CREAR CUENTA
    @PostMapping("/{clienteId}/cuentas")
    public ResponseEntity<CuentaDto> crearCuenta(@PathVariable Long clienteId, @RequestBody CuentaDto cuentaDto) {
        cuentaDto.setClienteId(clienteId);
        CuentaDto nueva = cuentaService.crearCuenta(cuentaDto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    //REST API ELIMINAR CUENTA
    @DeleteMapping("/{clienteId}/cuentas/{cuentaId}")
    public ResponseEntity<?> eliminarCuenta(
            @PathVariable Long clienteId,
            @PathVariable Long cuentaId) {
        try {
            cuentaService.eliminarCuenta(clienteId, cuentaId);
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    //Solo para que la pagina muestre el nombre del cliente
    @GetMapping("/yo")
    public ResponseEntity<?> obtenerClienteActual(Authentication authentication) {
        String email = authentication.getName();
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("nombre", cliente.get().getNombre());

        return ResponseEntity.ok(data);
    }

    //REST API BALANCE TOTAL
    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> obtenerBalanceCliente(@PathVariable Long id) {
        double balanceTotal = clienteService.obtenerBalanceTotalCliente(id);
        return ResponseEntity.ok(balanceTotal);
    }

    @GetMapping("/yo/balance")
    public ResponseEntity<Double> obtenerBalanceClienteActual(@RequestHeader("Authorization") String token) {
        try {
            Cliente cliente = clienteService.obtenerClienteAutenticado(token);
            double balance = clienteService.obtenerBalanceTotalCliente(cliente.getId());
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}