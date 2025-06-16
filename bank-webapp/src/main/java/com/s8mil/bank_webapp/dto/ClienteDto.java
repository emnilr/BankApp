package com.s8mil.bank_webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClienteDto  {
    private UsuarioDto usuario;
    private double balance;
    private List<CuentaDto> cuentas;
}
