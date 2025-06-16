package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Transaccion;
import com.s8mil.bank_webapp.entity.Usuario;
import com.s8mil.bank_webapp.mapper.UsuarioMapper;
import com.s8mil.bank_webapp.repository.RepositorioCuentas;
import com.s8mil.bank_webapp.repository.RepositorioTransacciones;
import com.s8mil.bank_webapp.repository.RepositorioUsuarios;
import com.s8mil.bank_webapp.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final RepositorioTransacciones repositorioTransacciones;
    private final RepositorioCuentas repositorioCuentas;
    private final RepositorioUsuarios repositorioUsuarios;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuarios repositorioUsuarios, PasswordEncoder passwordEncoder, RepositorioTransacciones repositorioTransacciones, RepositorioCuentas repositorioCuentas) {
        this.repositorioUsuarios = repositorioUsuarios;
        this.passwordEncoder = passwordEncoder;
        this.repositorioTransacciones = repositorioTransacciones;
        this.repositorioCuentas = repositorioCuentas;
    }
    @Override
    public UsuarioDto getAccountById(Long id) {
        Usuario usuario = repositorioUsuarios.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));
        return UsuarioMapper.mapToUsuarioDto(usuario);
    }

    @Override
    public List<UsuarioDto> getAllUsuarios() {
        List<Usuario> usuarios = repositorioUsuarios.findAll();
        return usuarios.stream().map(usuario -> UsuarioMapper.mapToUsuarioDto(usuario)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = repositorioUsuarios.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;

            for (Cuenta cuenta : cliente.getCuentas()) {
                List<Transaccion> transacciones = repositorioTransacciones.findByCuentaId(cuenta.getId());

                for (Transaccion tx : transacciones) {
                    List<Transaccion> relacionadas = repositorioTransacciones.findByTransaccionRelacionada(tx);
                    for (Transaccion tRelacionada : relacionadas) {
                        tRelacionada.setTransaccionRelacionada(null);
                    }
                    repositorioTransacciones.saveAll(relacionadas);
                }

                repositorioTransacciones.deleteAll(transacciones);

                repositorioCuentas.deleteById(cuenta.getId());
            }
        }

        repositorioUsuarios.deleteById(id);
    }

    @Override
    public UsuarioDto crearUsuario(UsuarioDto usuarioDto) {
        if (repositorioUsuarios.existsByEmail(usuarioDto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }
        Usuario usuario = UsuarioMapper.mapToUsuario(usuarioDto);

        // HASH
        String hashedPassword = passwordEncoder.encode(usuario.getContraseña());
        usuario.setContraseña(hashedPassword);

        Usuario savedUsuario = repositorioUsuarios.save(usuario);
        return UsuarioMapper.mapToUsuarioDto(savedUsuario);
    }

    private Usuario obtenerClientePorId(Long id){
        return repositorioUsuarios.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

    }

    //ACTUALIZAR USUARIO
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuario = repositorioUsuarios.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setTelefono(usuarioActualizado.getTelefono());

        return repositorioUsuarios.save(usuario);
    }
}


