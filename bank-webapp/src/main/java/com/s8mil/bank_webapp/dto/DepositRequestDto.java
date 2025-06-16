package com.s8mil.bank_webapp.dto;

import lombok.Data;

@Data
public class DepositRequestDto {
    private double monto;

    public double getMonto() { return monto; }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}