package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.dto.CuentaDto;
import com.s8mil.bank_webapp.dto.TransaccionDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.entity.Transaccion;
import com.s8mil.bank_webapp.mapper.CuentaMapper;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.repository.RepositorioCuentas;
import com.s8mil.bank_webapp.repository.RepositorioTransacciones;
import com.s8mil.bank_webapp.service.CuentaService;
import com.s8mil.bank_webapp.service.TransaccionService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final RepositorioCuentas repositorioCuentas;
    private final TransaccionService transaccionService;
    private final RepositorioTransacciones repositorioTransacciones;
    private final RepositorioClientes repositorioClientes;

    public CuentaServiceImpl(RepositorioCuentas repositorioCuentas, TransaccionService transaccionService, RepositorioTransacciones repositorioTransacciones, RepositorioClientes repositorioClientes) {
        this.repositorioCuentas = repositorioCuentas;
        this.transaccionService = transaccionService;
        this.repositorioTransacciones = repositorioTransacciones;
        this.repositorioClientes = repositorioClientes;
    }

    @Override
    public CuentaDto crearCuenta(CuentaDto cuentaDto) {
        Long clienteId = cuentaDto.getClienteId();

        long cantidadCuentas = repositorioCuentas.countByClienteId(clienteId);

        if (cantidadCuentas >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya tiene el número máximo de cuentas permitidas (10)");
        }

        String numeroCuenta = "000" + clienteId + (cantidadCuentas + 1);
        cuentaDto.setNumeroCuenta(numeroCuenta);

        Cuenta cuenta = CuentaMapper.mapToCuenta(cuentaDto);
        Cuenta savedCuenta = repositorioCuentas.save(cuenta);
        return CuentaMapper.mapToCuentaDto(savedCuenta);
    }

    @Override
    public CuentaDto getCuentaById(Long id) {
        Cuenta cuenta = obtenerCuentaPorId(id);
        return CuentaMapper.mapToCuentaDto(cuenta);
    }

    @Override
    public List<CuentaDto> getAllCuentas() {
        return repositorioCuentas.findAll()
                .stream()
                .map(CuentaMapper::mapToCuentaDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarCuenta(Long clienteId, Long cuentaId) {
        Cuenta cuenta = repositorioCuentas.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (!cuenta.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("El cliente no es dueño de esta cuenta");
        }

        if (cuenta.getTransacciones().size() > 1) {
            throw new RuntimeException("La cuenta tiene transacciones y no se puede eliminar");
        }

        Iterator<Transaccion> iterator = cuenta.getTransacciones().iterator();
        while (iterator.hasNext()) {
            Transaccion transaccion = iterator.next();
            transaccion.setCuenta(null);
            iterator.remove();
        }

        cuenta.setCliente(null);
        repositorioCuentas.delete(cuenta); 
    }


    @Override
    public CuentaDto deposit(Long cuentaId, double monto) {
        Cuenta cuenta = obtenerCuentaPorId(cuentaId);
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        Cuenta actualizada = repositorioCuentas.save(cuenta);

        // Registrar la transacción
        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setCuentaId(cuentaId);
        transaccion.setMonto(monto);
        transaccion.setTipo("DEPOSITO");
        transaccionService.crearTransaccion(transaccion);
        Cliente cliente = cuenta.getCliente();
        actualizarBalanceCliente(cliente);

        return CuentaMapper.mapToCuentaDto(actualizada);
    }

    @Override
    public CuentaDto withdraw(Long cuentaId, double monto) {
        Cuenta cuenta = obtenerCuentaPorId(cuentaId);
        if (cuenta.getSaldo() < monto) {
            throw new RuntimeException("Fondos insuficientes");
        }
        cuenta.setSaldo(cuenta.getSaldo() - monto);
        Cuenta actualizada = repositorioCuentas.save(cuenta);

        TransaccionDto transaccion = new TransaccionDto();
        transaccion.setCuentaId(cuentaId);
        transaccion.setMonto(monto);
        transaccion.setTipo("RETIRO");
        transaccionService.crearTransaccion(transaccion);
        Cliente cliente = cuenta.getCliente();
        actualizarBalanceCliente(cliente);
        return CuentaMapper.mapToCuentaDto(actualizada);
    }

    @Override
    public List<CuentaDto> obtenerCuentasPorClienteId(Long clienteId) {
        List<Cuenta> cuentas = repositorioCuentas.findByClienteId(clienteId);
        return cuentas.stream()
                .map(CuentaMapper::mapToCuentaDto)
                .collect(Collectors.toList());
    }

    private Cuenta obtenerCuentaPorId(Long id) {
        return repositorioCuentas.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    private void actualizarBalanceCliente(Cliente cliente) {
        double nuevoBalance = cliente.getCuentas().stream()
                .mapToDouble(Cuenta::getSaldo)
                .sum();
        cliente.setBalance(nuevoBalance);
        repositorioClientes.save(cliente);
    }

}
