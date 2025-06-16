package com.s8mil.bank_webapp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("EMPLEADO")
@Getter
@Setter
public class Empleado extends Usuario {
}