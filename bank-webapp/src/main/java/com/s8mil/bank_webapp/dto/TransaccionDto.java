package com.s8mil.bank_webapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransaccionDto {
    private Long id;
    private double monto;
    private String tipo;
    private LocalDateTime fecha;
    private Long cuentaId;
    private String numeroCuenta;
    private String numeroCuentaDestinatario;

    public String getNumeroCuentaDestinatario() {
        return numeroCuentaDestinatario;
    }

    public void setNumeroCuentaDestinatario(String numeroCuentaDestinatario) {
        this.numeroCuentaDestinatario = numeroCuentaDestinatario;
    }
}
