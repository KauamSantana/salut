package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.VisitaCadastroDTO;
import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.model.Visita;
import br.com.salut.salutbackend.repository.ClienteRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import br.com.salut.salutbackend.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visitas")
public class VisitaController {

    @Autowired private VisitaRepository visitaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private RepresentanteRepository representanteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Visita> criarVisita(@RequestBody VisitaCadastroDTO data) {
        Cliente cliente = clienteRepository.findById(data.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para a visita"));
        Representante representante = representanteRepository.findById(data.representanteId())
                .orElseThrow(() -> new RuntimeException("Representante não encontrado para a visita"));

        Visita novaVisita = new Visita();
        novaVisita.setCliente(cliente);
        novaVisita.setRepresentante(representante);
        novaVisita.setDataHora(data.dataHora());
        novaVisita.setObservacoes(data.observacoes());
        novaVisita.setStatus("AGENDADA");

        Visita visitaSalva = visitaRepository.save(novaVisita);
        return ResponseEntity.ok(visitaSalva);
    }

    @GetMapping
    public List<Visita> listarVisitas(
            @RequestParam Optional<Long> clienteId,
            @RequestParam Optional<Long> representanteId) {

        if (clienteId.isPresent()) {
            return visitaRepository.findByClienteId(clienteId.get());
        }
        if (representanteId.isPresent()) {
            return visitaRepository.findByRepresentanteId(representanteId.get());
        }
        return visitaRepository.findAll();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Visita> atualizarVisita(@PathVariable Long id, @RequestBody VisitaCadastroDTO detalhesVisita) {
        return visitaRepository.findById(id)
                .map(visita -> {
                    if (detalhesVisita.dataHora() != null) {
                        visita.setDataHora(detalhesVisita.dataHora());
                    }
                    if (detalhesVisita.status() != null) {
                        visita.setStatus(detalhesVisita.status());
                    }
                    if (detalhesVisita.observacoes() != null) {
                        visita.setObservacoes(detalhesVisita.observacoes());
                    }
                    Visita visitaAtualizada = visitaRepository.save(visita);
                    return ResponseEntity.ok(visitaAtualizada);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVisita(@PathVariable Long id) {
        if (!visitaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        visitaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}