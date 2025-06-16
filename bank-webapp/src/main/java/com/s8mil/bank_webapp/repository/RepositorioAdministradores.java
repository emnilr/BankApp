package com.s8mil.bank_webapp.repository;

import com.s8mil.bank_webapp.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAdministradores extends JpaRepository<Administrador, Long> {

}