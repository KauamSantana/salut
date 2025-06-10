package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.ClienteRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RepresentanteRepository representanteRepository;

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        // Se um representante foi enviado na requisição...
        if (cliente.getRepresentante() != null && cliente.getRepresentante().getId() != null) {
            // ...buscamos ele completo no banco de dados...
            Representante representante = representanteRepository.findById(cliente.getRepresentante().getId())
                    .orElseThrow(() -> new RuntimeException("Representante responsável não encontrado"));
            // ...e associamos ao novo cliente.
            cliente.setRepresentante(representante);
        }
        return clienteRepository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente detalhesCliente) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(detalhesCliente.getNome());
                    cliente.setSobrenome(detalhesCliente.getSobrenome());
                    cliente.setCnpjCpf(detalhesCliente.getCnpjCpf());
                    cliente.setEmail(detalhesCliente.getEmail());
                    cliente.setTelefone(detalhesCliente.getTelefone());
                    cliente.setEndereco(detalhesCliente.getEndereco());
                    cliente.setResponsavel(detalhesCliente.getResponsavel());
                    cliente.setContatos(detalhesCliente.getContatos());
                    cliente.setLatitude(detalhesCliente.getLatitude());
                    cliente.setLongitude(detalhesCliente.getLongitude());
                    // A lógica para atualizar o representante associado pode ser adicionada aqui se necessário
                    Cliente clienteAtualizado = clienteRepository.save(cliente);
                    return ResponseEntity.ok(clienteAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}