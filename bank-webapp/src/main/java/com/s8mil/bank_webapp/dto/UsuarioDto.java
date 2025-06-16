package com.s8mil.bank_webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String email;
    private String contraseña;
    private String telefono;
    private String tipo_usuario;

}
