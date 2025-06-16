package com.s8mil.bank_webapp.repository;

import com.s8mil.bank_webapp.entity.Transaccion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RepositorioTransacciones extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaId(Long cuentaId);
    List<Transaccion> findByCuenta_Cliente_Id(Long clienteId);
    long countByCuentaId(Long cuentaId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Transaccion t WHERE t.cuenta.id = :cuentaId")
    void deleteByCuentaId(@Param("cuentaId") Long cuentaId);

    @Query("""
    SELECT t FROM Transaccion t
    WHERE t.tipo = 'TRANSFERENCIA_ENTRADA'
    AND t.monto = :monto
    AND ABS(TIMESTAMPDIFF(SECOND, t.fecha, :fechaReferencia)) <= 1
    AND t.cuenta.numeroCuenta <> :numeroCuentaOrigen
    """)
    Optional<Transaccion> findTransferenciaEntradaMatch(
            @Param("monto") double monto,
            @Param("fechaReferencia") LocalDateTime fechaReferencia,
            @Param("numeroCuentaOrigen") String numeroCuentaOrigen
    );
    List<Transaccion> findByTransaccionRelacionada(Transaccion transaccion);

}