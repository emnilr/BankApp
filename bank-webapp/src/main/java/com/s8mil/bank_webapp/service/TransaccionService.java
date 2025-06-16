package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.TransaccionDto;

import java.util.List;

public interface TransaccionService {
    TransaccionDto crearTransaccion(TransaccionDto transaccionDto);
    List<TransaccionDto> getAllTransacciones();
    List<TransaccionDto> getTransaccionesPorCuenta(Long cuentaId);
    List<TransaccionDto> getTransaccionesPorCliente(Long clienteId);
}
