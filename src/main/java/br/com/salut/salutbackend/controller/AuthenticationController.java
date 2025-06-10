package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.AuthenticationDTO;
import br.com.salut.salutbackend.dto.LoginResponseDTO; // NOVO IMPORT
import br.com.salut.salutbackend.model.Representante; // NOVO IMPORT
import br.com.salut.salutbackend.service.TokenService; // NOVO IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService; // INJETANDO O NOVO SERVIÇO

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getSenha());
        var authentication = manager.authenticate(authenticationToken);

        // Se chegou até aqui, o login foi um sucesso.
        // Pegamos o usuário autenticado e geramos o token para ele.
        var token = tokenService.gerarToken((Representante) authentication.getPrincipal());

        // Retornamos o token dentro do nosso DTO de resposta.
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}