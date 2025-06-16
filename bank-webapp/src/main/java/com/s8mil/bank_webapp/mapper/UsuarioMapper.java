package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Administrador;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Empleado;
import com.s8mil.bank_webapp.entity.Usuario;

public class UsuarioMapper{
    public static Usuario mapToUsuario(UsuarioDto usuarioDto){
        switch (usuarioDto.getTipo_usuario()) {
            case "CLIENTE": {
                Cliente cliente = new Cliente();
                cliente.setId(usuarioDto.getId());
                cliente.setNombre(usuarioDto.getNombre());
                cliente.setEmail(usuarioDto.getEmail());
                cliente.setContraseña(usuarioDto.getContraseña());
                cliente.setTelefono(usuarioDto.getTelefono());
                return cliente;
            }
            case "EMPLEADO": {
                Empleado empleado = new Empleado();
                empleado.setId(usuarioDto.getId());
                empleado.setNombre(usuarioDto.getNombre());
                empleado.setEmail(usuarioDto.getEmail());
                empleado.setContraseña(usuarioDto.getContraseña());
                empleado.setTelefono(usuarioDto.getTelefono());
                return empleado;
            }
            case "ADMINISTRADOR": {
                Administrador admin = new Administrador();
                admin.setId(usuarioDto.getId());
                admin.setNombre(usuarioDto.getNombre());
                admin.setEmail(usuarioDto.getEmail());
                admin.setContraseña(usuarioDto.getContraseña());
                admin.setTelefono(usuarioDto.getTelefono());
                return admin;
            }
            default: {
                Usuario usuario = new Usuario();
                usuario.setId(usuarioDto.getId());
                usuario.setNombre(usuarioDto.getNombre());
                usuario.setEmail(usuarioDto.getEmail());
                usuario.setContraseña(usuarioDto.getContraseña());
                usuario.setTelefono(usuarioDto.getTelefono());
                return usuario;
            }
        }
    }

    public static UsuarioDto mapToUsuarioDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setContraseña(usuario.getContraseña());
        dto.setTelefono(usuario.getTelefono());

        if (usuario instanceof Cliente) {
            dto.setTipo_usuario("CLIENTE");
        } else if (usuario instanceof Empleado) {
            dto.setTipo_usuario("EMPLEADO");
        } else if (usuario instanceof Administrador) {
            dto.setTipo_usuario("ADMINISTRADOR");
        } else {
            dto.setTipo_usuario("USUARIO");
        }

        return dto;
    }
}


