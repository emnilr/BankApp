package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.TransaccionDto;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Transaccion;

public class TransaccionMapper {
    public static Transaccion mapToTransaccion(TransaccionDto dto, Cuenta cuenta) {
        Transaccion t = new Transaccion();
        t.setId(dto.getId());
        t.setMonto(dto.getMonto());
        t.setTipo(dto.getTipo());
        t.setFecha(dto.getFecha());
        t.setCuenta(cuenta);
        return t;
    }

    public static TransaccionDto mapToTransaccionDto(Transaccion t) {
        TransaccionDto dto = new TransaccionDto();
        dto.setId(t.getId());
        dto.setMonto(t.getMonto());
        dto.setTipo(t.getTipo());
        dto.setFecha(t.getFecha());
        dto.setCuentaId(t.getCuenta().getId());
        return dto;
    }
}
