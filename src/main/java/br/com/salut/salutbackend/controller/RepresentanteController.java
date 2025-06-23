package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.RepresentanteCadastroDTO;
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/representantes")
public class RepresentanteController {

    @Autowired
    private RepresentanteRepository representanteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public Representante criarRepresentante(@RequestBody RepresentanteCadastroDTO data) {
        Representante novoRepresentante = new Representante();
        novoRepresentante.setNome(data.nome());
        novoRepresentante.setSobrenome(data.sobrenome());
        novoRepresentante.setEmail(data.email());
        novoRepresentante.setTelefone(data.telefone());
        novoRepresentante.setRegiao(data.regiao());
        novoRepresentante.setStatus(data.status());
        novoRepresentante.setSenha(passwordEncoder.encode(data.senha()));
        novoRepresentante.setMeta(data.meta());
        novoRepresentante.setTaxaComissao(data.taxaComissao());
        novoRepresentante.setRole(data.role() != null ? data.role() : "USER");

        return representanteRepository.save(novoRepresentante);
    }

    // MÉTODO LISTAR ATUALIZADO PARA PAGINAÇÃO
    @GetMapping
    public Page<Representante> listarRepresentantes(Pageable pageable) {
        return representanteRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Representante> buscarRepresentantePorId(@PathVariable Long id) {
        return representanteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Representante> atualizarRepresentante(@PathVariable Long id, @RequestBody Representante detalhesRepresentante) {
        return representanteRepository.findById(id)
                .map(representante -> {
                    // Campos que já estavam
                    representante.setNome(detalhesRepresentante.getNome());
                    representante.setEmail(detalhesRepresentante.getEmail());
                    if (detalhesRepresentante.getSenha() != null && !detalhesRepresentante.getSenha().isEmpty()) {
                        representante.setSenha(passwordEncoder.encode(detalhesRepresentante.getSenha()));
                    }

                    // ---> LINHAS QUE FALTAVAM <---
                    representante.setSobrenome(detalhesRepresentante.getSobrenome());
                    representante.setTelefone(detalhesRepresentante.getTelefone());
                    representante.setRegiao(detalhesRepresentante.getRegiao());
                    representante.setStatus(detalhesRepresentante.getStatus());
                    // ---------------------------------

                    Representante repAtualizado = representanteRepository.save(representante);
                    return ResponseEntity.ok(repAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRepresentante(@PathVariable Long id) {
        if (!representanteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        representanteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}