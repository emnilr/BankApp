package com.s8mil.bank_webapp.controller;

import com.s8mil.bank_webapp.dto.LoginRequest;
import com.s8mil.bank_webapp.dto.UsuarioDto;
import com.s8mil.bank_webapp.entity.Usuario;
import com.s8mil.bank_webapp.repository.RepositorioUsuarios;
import com.s8mil.bank_webapp.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService usuarioService;
    private final RepositorioUsuarios repositorioUsuarios;
    private final PasswordEncoder passwordEncoder;

    private final SecretKey secretKey;

    public AuthController(
            UsuarioService usuarioService,
            RepositorioUsuarios repositorioUsuarios,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String secretKeyString
    ) {
        this.usuarioService = usuarioService;
        this.repositorioUsuarios = repositorioUsuarios;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        Optional<Usuario> optionalUsuario = repositorioUsuarios.findByEmail(request.getEmail());
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();
        if (!passwordEncoder.matches(request.getContraseña(), usuario.getContraseña())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        String jwt = Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("tipo_usuario", usuario.getClass().getSimpleName())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registrarCliente(@RequestBody UsuarioDto nuevoCliente) {
        nuevoCliente.setTipo_usuario("CLIENTE");

        UsuarioDto creado = usuarioService.crearUsuario(nuevoCliente);

        String jwt = Jwts.builder()
                .setSubject(creado.getEmail())
                .claim("tipo_usuario", creado.getTipo_usuario())
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return ResponseEntity.ok(jwt);
    }
}
