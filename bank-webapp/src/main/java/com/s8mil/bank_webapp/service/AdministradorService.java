package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.AdministradorDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;

import java.util.List;

public interface AdministradorService {
    AdministradorDto crearAdministrador(AdministradorDto administradorDto);
    void eliminarAdministrador(Long id);


    UsuarioDto crearEmpleado(UsuarioDto empleadoDto);
    UsuarioDto crearCliente(UsuarioDto clienteDto);

    void eliminarEmpleado(Long id);
    void eliminarCliente(Long id);

    UsuarioDto editarUsuario(Long id, UsuarioDto usuarioDto);

    List<UsuarioDto> getTodosLosUsuarios();
}