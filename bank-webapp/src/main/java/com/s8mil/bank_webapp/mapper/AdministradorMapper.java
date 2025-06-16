package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.AdministradorDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Administrador;

public class AdministradorMapper {

    public static Administrador mapToAdministrador(AdministradorDto administradorDto) {
        Administrador administrador = new Administrador();

        administrador.setId(administradorDto.getUsuario().getId());
        administrador.setNombre(administradorDto.getUsuario().getNombre());
        administrador.setEmail(administradorDto.getUsuario().getEmail());
        administrador.setContraseña(administradorDto.getUsuario().getContraseña());
        administrador.setTelefono(administradorDto.getUsuario().getTelefono());

        return administrador;
    }

    public static AdministradorDto mapToAdministradorDto(Administrador administrador) {
        UsuarioDto usuarioDto = new UsuarioDto();

        usuarioDto.setId(administrador.getId());
        usuarioDto.setNombre(administrador.getNombre());
        usuarioDto.setEmail(administrador.getEmail());
        usuarioDto.setContraseña(administrador.getContraseña());
        usuarioDto.setTelefono(administrador.getTelefono());

        AdministradorDto administradorDto = new AdministradorDto();
        administradorDto.setUsuario(usuarioDto);

        return administradorDto;
    }
}