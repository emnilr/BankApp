package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Usuario;
import com.s8mil.bank_webapp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //REST API AÑADIR USUARIO
    @PostMapping
    public ResponseEntity<UsuarioDto> añadirUsuario(@RequestBody UsuarioDto usuarioDto){
        return new ResponseEntity<>(usuarioService.crearUsuario(usuarioDto), HttpStatus.CREATED);
    }

    //REST API GET USUARIO
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> obtenerUsuario(@PathVariable Long id){
        UsuarioDto usuarioDto = usuarioService.getAccountById(id);
        return ResponseEntity.ok(usuarioDto);
    }

    //REST API TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios(){
        List<UsuarioDto> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    //REST API ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado");
    }


    //REST API ACTUALIZAR USUARIO
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
