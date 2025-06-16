package com.s8mil.bank_webapp.service.impl;

import com.s8mil.bank_webapp.config.JwtService;
import com.s8mil.bank_webapp.dto.ClienteDto;
import com.s8mil.bank_webapp.entity.Cliente;
import com.s8mil.bank_webapp.entity.Cuenta;
import com.s8mil.bank_webapp.mapper.ClienteMapper;
import com.s8mil.bank_webapp.repository.RepositorioClientes;
import com.s8mil.bank_webapp.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final RepositorioClientes repositorioClientes;
    private final JwtService jwtService;

    public ClienteServiceImpl(RepositorioClientes repositorioClientes, JwtService jwtService) {
        this.repositorioClientes = repositorioClientes;
        this.jwtService = jwtService;
    }

    @Override
    public ClienteDto crearCliente(ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.mapToCliente(clienteDto);
        Cliente savedCliente = repositorioClientes.save(cliente);
        return ClienteMapper.mapToClienteDto(savedCliente);
    }

    @Override
    public ClienteDto getClientbyId(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        return ClienteMapper.mapToClienteDto(cliente);
    }

    @Override
    public ClienteDto deposit(Long id, double monto) {
        Cliente cliente = obtenerClientePorId(id);
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        cliente.setBalance(cliente.getBalance() + monto);
        Cliente actualizado = repositorioClientes.save(cliente);
        return ClienteMapper.mapToClienteDto(actualizado);
    }

    @Override
    public ClienteDto withdraw(Long id, double monto) {
        Cliente cliente = obtenerClientePorId(id);

        if(cliente.getBalance() < monto){
            throw new RuntimeException("El monto supera su saldo");
        }
        cliente.setBalance(cliente.getBalance() - monto);
        Cliente actualizado = repositorioClientes.save(cliente);
        return ClienteMapper.mapToClienteDto(actualizado);
    }

    private Cliente obtenerClientePorId(Long id) {
        return repositorioClientes.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public List<ClienteDto> getAllClientes() {
        List<Cliente> clientes = repositorioClientes.findAll();
        return clientes.stream().map(cliente -> ClienteMapper.mapToClienteDto(cliente)).collect(Collectors.toList());
    }

    @Override
    public void eliminarCliente(Long id) {
        obtenerClientePorId(id);
        repositorioClientes.deleteById(id);
    }

    @Override
    public double obtenerBalanceTotalCliente(Long clienteId) {
        Cliente cliente = repositorioClientes.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return cliente.getCuentas().stream()
                .mapToDouble(Cuenta::getSaldo)
                .sum();
    }

    public Cliente obtenerClienteAutenticado(String token) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtService.extractUsername(jwt);

        return repositorioClientes.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
    }


}