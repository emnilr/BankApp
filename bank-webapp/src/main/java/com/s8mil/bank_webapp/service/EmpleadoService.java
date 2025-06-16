package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.dto.CuentaDto;

import java.util.List;

public interface EmpleadoService {
    CuentaDto crearCuentaParaCliente(Long clienteId, CuentaDto cuentaDto);
    void cerrarCuenta(Long cuentaId);
    ClienteDto verClientePorId(Long clienteId);
    List<ClienteDto> verTodosLosClientes();
}