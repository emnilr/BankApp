package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    UsuarioDto crearUsuario(UsuarioDto usuarioDto);

    UsuarioDto getAccountById(Long id);

    List<UsuarioDto> getAllUsuarios();

    void eliminarUsuario(Long id);

    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);

}
