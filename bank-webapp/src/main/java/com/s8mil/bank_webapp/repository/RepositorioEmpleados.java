package com.s8mil.bank_webapp.repository;

import com.s8mil.bank_webapp.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEmpleados extends JpaRepository<Empleado, Long> {
}
