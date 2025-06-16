package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.service.ClienteService;
import com.s8mil.bank_webapp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public EmpleadoController(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    // REST API TODOS LOS CLIENTES
    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioDto>> verClientes() {
        List<UsuarioDto> clientes = usuarioService.getAllUsuarios(); // puedes filtrar solo CLIENTE si quieres
        return ResponseEntity.ok(clientes);
    }

    // REST API CLIENTE POR ID
    @GetMapping("/clientes/{id}")
    public ResponseEntity<UsuarioDto> verClientePorId(@PathVariable Long id) {
        UsuarioDto cliente = usuarioService.getAccountById(id);
        return ResponseEntity.ok(cliente);
    }

    // REST API CREAR CLIENTE
    @PostMapping("/clientes")
    public ResponseEntity<ClienteDto> crearCliente(@RequestBody ClienteDto clienteDto) {
        ClienteDto creado = clienteService.crearCliente(clienteDto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    // REST API ELIMINAR CLIENTE
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
