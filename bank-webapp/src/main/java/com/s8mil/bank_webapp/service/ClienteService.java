package com.s8mil.bank_webapp.service;

import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.entity.Cliente;

import java.util.List;

public interface ClienteService {
    ClienteDto crearCliente(ClienteDto clienteDto);
    ClienteDto getClientbyId(Long id);
    ClienteDto deposit(Long id, double monto);
    ClienteDto withdraw(Long id, double monto);
    List<ClienteDto> getAllClientes();
    void eliminarCliente(Long id);
    double obtenerBalanceTotalCliente(Long clienteId);
    Cliente obtenerClienteAutenticado(String token);
}