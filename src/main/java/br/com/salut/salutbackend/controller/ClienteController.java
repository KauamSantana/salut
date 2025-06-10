package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
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

    // NOVO MÉTODO PARA ATUALIZAR UM CLIENTE
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente detalhesCliente) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(detalhesCliente.getNome());
                    cliente.setCnpjCpf(detalhesCliente.getCnpjCpf());
                    cliente.setEndereco(detalhesCliente.getEndereco());
                    cliente.setResponsavel(detalhesCliente.getResponsavel());
                    cliente.setContatos(detalhesCliente.getContatos());
                    cliente.setLatitude(detalhesCliente.getLatitude());
                    cliente.setLongitude(detalhesCliente.getLongitude());
                    Cliente clienteAtualizado = clienteRepository.save(cliente);
                    return ResponseEntity.ok(clienteAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    // NOVO MÉTODO PARA DELETAR UM CLIENTE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}