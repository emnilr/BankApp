package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.dto.TransaccionDto;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Transaccion;
import com.s8mil.bank_webapp.mapper.TransaccionMapper;
import com.s8mil.bank_webapp.repository.RepositorioCuentas;
import com.s8mil.bank_webapp.repository.RepositorioTransacciones;
import com.s8mil.bank_webapp.service.TransaccionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final RepositorioTransacciones repositorioTransacciones;
    private final RepositorioCuentas repositorioCuentas;

    public TransaccionServiceImpl(RepositorioTransacciones rt, RepositorioCuentas rc) {
        this.repositorioTransacciones = rt;
        this.repositorioCuentas = rc;
    }

    @Override
    public TransaccionDto crearTransaccion(TransaccionDto dto) {
        Cuenta cuenta = repositorioCuentas.findById(dto.getCuentaId())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));


        Transaccion transaccion = TransaccionMapper.mapToTransaccion(dto, cuenta);
        transaccion.setFecha(LocalDateTime.now());

        Transaccion saved = repositorioTransacciones.save(transaccion);

        return TransaccionMapper.mapToTransaccionDto(saved);
    }

    @Override
    public List<TransaccionDto> getAllTransacciones() {
        List<Transaccion> transacciones = repositorioTransacciones.findAll();
        List<TransaccionDto> resultado = new ArrayList<>();

        for (Transaccion tx : transacciones) {
            TransaccionDto dto = new TransaccionDto();
            dto.setId(tx.getId());
            dto.setFecha(tx.getFecha());
            dto.setTipo(tx.getTipo());
            dto.setMonto(tx.getMonto());
            dto.setNumeroCuenta(tx.getCuenta().getNumeroCuenta());

            if ("TRANSFERENCIA_SALIDA".equals(tx.getTipo()) && tx.getTransaccionRelacionada() != null) {
                dto.setNumeroCuentaDestinatario(tx.getTransaccionRelacionada().getCuenta().getNumeroCuenta());
            }

            resultado.add(dto);
        }

        return resultado;
    }
    @Override
    public List<TransaccionDto> getTransaccionesPorCuenta(Long cuentaId) {
        return repositorioTransacciones.findByCuentaId(cuentaId)
                .stream()
                .map(TransaccionMapper::mapToTransaccionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionDto> getTransaccionesPorCliente(Long clienteId) {
        return repositorioTransacciones.findByCuenta_Cliente_Id(clienteId)
                .stream()
                .map(TransaccionMapper::mapToTransaccionDto)
                .collect(Collectors.toList());
    }
}
