package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Vinho;
import br.com.salut.salutbackend.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // NOVO MÉTODO PARA ATUALIZAR UM VINHO
    @PutMapping("/{id}")
    public ResponseEntity<Vinho> atualizarVinho(@PathVariable Long id, @RequestBody Vinho detalhesVinho) {
        return vinhoRepository.findById(id)
                .map(vinho -> {
                    vinho.setNome(detalhesVinho.getNome());
                    vinho.setSafra(detalhesVinho.getSafra());
                    vinho.setTipo(detalhesVinho.getTipo());
                    vinho.setPrecoUnitario(detalhesVinho.getPrecoUnitario());
                    // Adicione outros campos que possam ser atualizados
                    Vinho vinhoAtualizado = vinhoRepository.save(vinho);
                    return ResponseEntity.ok(vinhoAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    // NOVO MÉTODO PARA DELETAR UM VINHO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVinho(@PathVariable Long id) {
        if (!vinhoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vinhoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, sucesso sem corpo
    }
}