package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.CuentaDto;

import java.util.List;

public interface CuentaService {
    CuentaDto crearCuenta(CuentaDto cuentaDto);
    CuentaDto getCuentaById(Long id);
    List<CuentaDto> getAllCuentas();
    void eliminarCuenta(Long clienteId, Long cuentaId);
    CuentaDto deposit(Long id, double monto);
    CuentaDto withdraw(Long id, double monto);
    List<CuentaDto> obtenerCuentasPorClienteId(Long clienteId);
}
