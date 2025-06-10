package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Vinho;
import br.com.salut.salutbackend.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vinhos")
public class VinhoController {

    @Autowired
    private VinhoRepository vinhoRepository;

    @PostMapping
    public Vinho criarVinho(@RequestBody Vinho vinho) {
        return vinhoRepository.save(vinho);
    }

    @GetMapping
    public List<Vinho> listarVinhos() {
        return vinhoRepository.findAll();
    }
}