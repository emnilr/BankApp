package com.s8mil.bank_webapp.dto;

import lombok.Data;

@Data
public class WithdrawRequestDto {
    private double monto;

    public double getMonto() { return monto; }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}