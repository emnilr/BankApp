package com.s8mil.bank_webapp.mapper;

import com.s8mil.bank_webapp.dto.EmpleadoDto;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Empleado;

public class EmpleadoMapper {

    public static EmpleadoDto mapToEmpleadoDto(Empleado empleado) {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(empleado.getId());
        usuarioDto.setNombre(empleado.getNombre());
        usuarioDto.setEmail(empleado.getEmail());
        usuarioDto.setContraseña(empleado.getContraseña());
        usuarioDto.setTelefono(empleado.getTelefono());

        EmpleadoDto empleadoDto = new EmpleadoDto();
        empleadoDto.setUsuario(usuarioDto);

        return empleadoDto;
    }

    public static Empleado mapToEmpleado(EmpleadoDto empleadoDto) {
        UsuarioDto usuarioDto = empleadoDto.getUsuario();

        Empleado empleado = new Empleado();
        empleado.setId(usuarioDto.getId());
        empleado.setNombre(usuarioDto.getNombre());
        empleado.setEmail(usuarioDto.getEmail());
        empleado.setContraseña(usuarioDto.getContraseña());
        empleado.setTelefono(usuarioDto.getTelefono());

        return empleado;
    }
}
