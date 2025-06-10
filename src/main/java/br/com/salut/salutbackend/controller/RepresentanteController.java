package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // NOVO MÉTODO PARA BUSCAR UM REPRESENTANTE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Representante> buscarRepresentantePorId(@PathVariable Long id) {
        return representanteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NOVO MÉTODO PARA ATUALIZAR UM REPRESENTANTE
    @PutMapping("/{id}")
    public ResponseEntity<Representante> atualizarRepresentante(@PathVariable Long id, @RequestBody Representante detalhesRepresentante) {
        return representanteRepository.findById(id)
                .map(representante -> {
                    representante.setNome(detalhesRepresentante.getNome());
                    representante.setEmail(detalhesRepresentante.getEmail());

                    // Se uma nova senha for enviada, criptografa ela também
                    if (detalhesRepresentante.getSenha() != null && !detalhesRepresentante.getSenha().isEmpty()) {
                        representante.setSenha(passwordEncoder.encode(detalhesRepresentante.getSenha()));
                    }

                    Representante repAtualizado = representanteRepository.save(representante);
                    return ResponseEntity.ok(repAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    // NOVO MÉTODO PARA DELETAR UM REPRESENTANTE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRepresentante(@PathVariable Long id) {
        if (!representanteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        representanteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}