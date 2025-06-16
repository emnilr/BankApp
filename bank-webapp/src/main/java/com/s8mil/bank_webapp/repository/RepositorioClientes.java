package com.s8mil.bank_webapp.repository;

import com.s8mil.bank_webapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioClientes extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}