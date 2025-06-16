package com.s8mil.bank_webapp.dto;

import lombok.Data;

@Data
public class CuentaDto {
    private Long id;
    private String numeroCuenta;
    private double saldo;
    private String tipo;
    private Long clienteId;
}