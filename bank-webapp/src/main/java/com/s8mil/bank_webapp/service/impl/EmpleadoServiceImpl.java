package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.dto.CuentaDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.mapper.ClienteMapper;
import com.s8mil.bank_webapp.mapper.CuentaMapper;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.repository.RepositorioCuentas;
import com.s8mil.bank_webapp.service.EmpleadoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final RepositorioClientes repositorioClientes;
    private final RepositorioCuentas repositorioCuentas;

    public EmpleadoServiceImpl(RepositorioClientes repositorioClientes, RepositorioCuentas repositorioCuentas) {
        this.repositorioClientes = repositorioClientes;
        this.repositorioCuentas = repositorioCuentas;
    }

    @Override
    public CuentaDto crearCuentaParaCliente(Long clienteId, CuentaDto cuentaDto) {
        Cliente cliente = repositorioClientes.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Cuenta cuenta = CuentaMapper.mapToCuenta(cuentaDto);
        cuenta.setCliente(cliente);

        Cuenta nuevaCuenta = repositorioCuentas.save(cuenta);
        return CuentaMapper.mapToCuentaDto(nuevaCuenta);
    }

    @Override
    public void cerrarCuenta(Long cuentaId) {
        repositorioCuentas.deleteById(cuentaId);
    }

    @Override
    public ClienteDto verClientePorId(Long clienteId) {
        Cliente cliente = repositorioClientes.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return ClienteMapper.mapToClienteDto(cliente);
    }

    @Override
    public List<ClienteDto> verTodosLosClientes() {
        return repositorioClientes.findAll()
                .stream()
                .map(ClienteMapper::mapToClienteDto)
                .collect(Collectors.toList());
    }
}
