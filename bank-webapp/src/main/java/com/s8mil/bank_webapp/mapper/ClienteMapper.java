package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.dto.CuentaDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    public static ClienteDto mapToClienteDto(Cliente cliente) {
        UsuarioDto usuarioDto = UsuarioMapper.mapToUsuarioDto(cliente);
        List<CuentaDto> cuentasDto = cliente.getCuentas().stream()
                .map(CuentaMapper::mapToCuentaDto)
                .collect(Collectors.toList());

        return new ClienteDto(usuarioDto, cliente.getBalance(), cuentasDto);
    }

    public static Cliente mapToCliente(ClienteDto clienteDto) {
        Usuario usuario = UsuarioMapper.mapToUsuario(clienteDto.getUsuario());

        Cliente cliente = new Cliente();
        cliente.setId(usuario.getId());
        cliente.setNombre(usuario.getNombre());
        cliente.setEmail(usuario.getEmail());
        cliente.setContraseña(usuario.getContraseña());
        cliente.setTelefono(usuario.getTelefono());

        cliente.setBalance(clienteDto.getBalance());

        if (clienteDto.getCuentas() != null) {
            cliente.setCuentas(clienteDto.getCuentas().stream()
                    .map(c -> {
                        Cuenta cuenta = CuentaMapper.mapToCuenta(c);
                        cuenta.setCliente(cliente);
                        return cuenta;
                    })
                    .collect(Collectors.toList()));
        }

        return cliente;
    }
}