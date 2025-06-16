package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.dto.AdministradorDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Administrador;
import com.s8mil.bank_webapp.entity.Usuario;
import com.s8mil.bank_webapp.mapper.UsuarioMapper;
import com.s8mil.bank_webapp.mapper.AdministradorMapper;
import com.s8mil.bank_webapp.repository.RepositorioAdministradores;
import com.s8mil.bank_webapp.repository.RepositorioUsuarios;
import com.s8mil.bank_webapp.service.AdministradorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final RepositorioAdministradores repositorioAdministradores;
    private final RepositorioUsuarios repositorioUsuarios;

    public AdministradorServiceImpl(RepositorioAdministradores repositorioAdministradores,
                                    RepositorioUsuarios repositorioUsuarios) {
        this.repositorioAdministradores = repositorioAdministradores;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    @Override
    public AdministradorDto crearAdministrador(AdministradorDto administradorDto) {
        Administrador admin = AdministradorMapper.mapToAdministrador(administradorDto);
        Administrador saved = repositorioAdministradores.save(admin);
        return AdministradorMapper.mapToAdministradorDto(saved);
    }

    @Override
    public void eliminarAdministrador(Long id) {
        repositorioAdministradores.deleteById(id);
    }

    @Override
    public UsuarioDto crearEmpleado(UsuarioDto empleadoDto) {
        Usuario empleado = UsuarioMapper.mapToUsuario(empleadoDto);
        Usuario saved = repositorioUsuarios.save(empleado);
        return UsuarioMapper.mapToUsuarioDto(saved);
    }

    @Override
    public UsuarioDto crearCliente(UsuarioDto clienteDto) {
        Usuario cliente = UsuarioMapper.mapToUsuario(clienteDto);
        Usuario saved = repositorioUsuarios.save(cliente);
        return UsuarioMapper.mapToUsuarioDto(saved);
    }

    @Override
    public void eliminarEmpleado(Long id) {
        repositorioUsuarios.deleteById(id);
    }

    @Override
    public void eliminarCliente(Long id) {
        repositorioUsuarios.deleteById(id);
    }

    @Override
    public UsuarioDto editarUsuario(Long id, UsuarioDto usuarioDto) {
        Usuario usuario = repositorioUsuarios.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(usuarioDto.getNombre());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setTelefono(usuarioDto.getTelefono());


        Usuario actualizado = repositorioUsuarios.save(usuario);
        return UsuarioMapper.mapToUsuarioDto(actualizado);
    }

    @Override
    public List<UsuarioDto> getTodosLosUsuarios() {
        return repositorioUsuarios.findAll()
                .stream()
                .map(UsuarioMapper::mapToUsuarioDto)
                .collect(Collectors.toList());
    }
}
