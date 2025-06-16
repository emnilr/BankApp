package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.CuentaDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;

public class CuentaMapper {

    public static CuentaDto mapToCuentaDto(Cuenta cuenta) {
        CuentaDto dto = new CuentaDto();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setTipo(cuenta.getTipo());
        if (cuenta.getCliente() != null) {
            dto.setClienteId(cuenta.getCliente().getId());
        }
        return dto;
    }

    public static Cuenta mapToCuenta(CuentaDto dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(dto.getId());
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setSaldo(dto.getSaldo());
        cuenta.setTipo(dto.getTipo());

        if (dto.getClienteId() != null) {
            Cliente cliente = new Cliente();
            cliente.setId(dto.getClienteId());
            cuenta.setCliente(cliente);
        }

        return cuenta;
    }
}
