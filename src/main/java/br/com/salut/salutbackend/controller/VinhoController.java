package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Vinho;
import br.com.salut.salutbackend.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/vinhos")
public class VinhoController {

    @Autowired
    private VinhoRepository vinhoRepository;

    @PostMapping
    public Vinho criarVinho(@RequestBody Vinho vinho) {
        return vinhoRepository.save(vinho);
    }

    // MÉTODO LISTAR ATUALIZADO PARA PAGINAÇÃO
    @GetMapping
    public Page<Vinho> listarVinhos(@RequestParam Optional<String> tipo, Pageable pageable) {
        if (tipo.isPresent()) {
            return vinhoRepository.findByTipo(tipo.get(), pageable);
        }
        return vinhoRepository.findAll(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vinho> atualizarVinho(@PathVariable Long id, @RequestBody Vinho detalhesVinho) {
        return vinhoRepository.findById(id)
                .map(vinho -> {
                    vinho.setNome(detalhesVinho.getNome());
                    vinho.setSafra(detalhesVinho.getSafra());
                    vinho.setTipo(detalhesVinho.getTipo());
                    vinho.setPrecoUnitario(detalhesVinho.getPrecoUnitario());
                    Vinho vinhoAtualizado = vinhoRepository.save(vinho);
                    return ResponseEntity.ok(vinhoAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVinho(@PathVariable Long id) {
        if (!vinhoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vinhoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}