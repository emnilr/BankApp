package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdministradorController {

    private final UsuarioService usuarioService;

    public AdministradorController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // REST API CREAR USUARIO
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDto> crearUsuario(@RequestBody UsuarioDto usuarioDto) {
        UsuarioDto nuevoUsuario = usuarioService.crearUsuario(usuarioDto);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // REST API TODOS LOS USUARIO
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    // REST API ELIMINAR USUARIO
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // REST API EDITAR USUARIO
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDto usuarioActualizado
    ) {
        usuarioActualizado.setId(id);
        UsuarioDto actualizado = usuarioService.crearUsuario(usuarioActualizado);
        return ResponseEntity.ok(actualizado);
    }
}
