package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.RepresentanteCadastroDTO; // NOVO IMPORT
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    // MUDANÇA AQUI: Recebemos o DTO em vez da Entidade
    public Representante criarRepresentante(@RequestBody RepresentanteCadastroDTO data) {
        // Criamos uma nova entidade Representante e copiamos os dados do DTO para ela
        Representante novoRepresentante = new Representante();
        novoRepresentante.setNome(data.nome());
        novoRepresentante.setSobrenome(data.sobrenome());
        novoRepresentante.setEmail(data.email());
        novoRepresentante.setTelefone(data.telefone());
        novoRepresentante.setRegiao(data.regiao());
        novoRepresentante.setStatus(data.status());
        novoRepresentante.setSenha(passwordEncoder.encode(data.senha())); // Criptografando a senha
        novoRepresentante.setMeta(data.meta());
        novoRepresentante.setTaxaComissao(data.taxaComissao());
        novoRepresentante.setRole(data.role() != null ? data.role() : "USER"); // Define USER como padrão

        return representanteRepository.save(novoRepresentante);
    }

    @GetMapping
    public List<Representante> listarRepresentantes() {
        return representanteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Representante> buscarRepresentantePorId(@PathVariable Long id) {
        return representanteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // O método de PUT também precisaria de um DTO, mas vamos focar no POST primeiro.
    @PutMapping("/{id}")
    public ResponseEntity<Representante> atualizarRepresentante(@PathVariable Long id, @RequestBody Representante detalhesRepresentante) {
        return representanteRepository.findById(id)
                .map(representante -> {
                    representante.setNome(detalhesRepresentante.getNome());
                    representante.setEmail(detalhesRepresentante.getEmail());
                    if (detalhesRepresentante.getSenha() != null && !detalhesRepresentante.getSenha().isEmpty()) {
                        representante.setSenha(passwordEncoder.encode(detalhesRepresentante.getSenha()));
                    }
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