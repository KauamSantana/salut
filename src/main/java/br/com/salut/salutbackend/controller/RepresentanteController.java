package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/representantes")
public class RepresentanteController {

    @Autowired
    private RepresentanteRepository representanteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Representante criarRepresentante(@RequestBody Representante representante) {
        // Criptografando a senha antes de salvar
        representante.setSenha(passwordEncoder.encode(representante.getSenha()));
        return representanteRepository.save(representante);
    }

    @GetMapping
    public List<Representante> listarRepresentantes() {
        return representanteRepository.findAll();
    }
}