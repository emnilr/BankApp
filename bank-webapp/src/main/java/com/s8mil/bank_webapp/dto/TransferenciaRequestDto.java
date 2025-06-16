package com.s8mil.bank_webapp.dto;

public class TransferenciaRequestDto {
    private String numeroCuenta;
    private Long cuentaOrigenId;
    private String numeroCuentaDestinatario;
    private double monto;

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public String getNumeroCuentaDestinatario() {
        return numeroCuentaDestinatario;
    }

    public void setNumeroCuentaDestinatario(String numeroCuentaDestinatario) {
        this.numeroCuentaDestinatario = numeroCuentaDestinatario;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}