package com.s8mil.bank_webapp.repository;

import com.s8mil.bank_webapp.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositorioCuentas extends JpaRepository<Cuenta, Long> {
    long countByClienteId(Long clienteId);
    List<Cuenta> findByClienteId(Long clienteId);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
