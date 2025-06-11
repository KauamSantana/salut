package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.ClienteCadastroDTO;
import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.ClienteRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Cliente criarCliente(@RequestBody ClienteCadastroDTO data) {
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(data.nome());
        novoCliente.setSobrenome(data.sobrenome());
        novoCliente.setCnpjCpf(data.cnpjCpf());
        novoCliente.setEmail(data.email());
        novoCliente.setTelefone(data.telefone());
        novoCliente.setEndereco(data.endereco());
        novoCliente.setResponsavel(data.responsavel());
        novoCliente.setContatos(data.contatos());
        novoCliente.setLatitude(data.latitude());
        novoCliente.setLongitude(data.longitude());

        if (data.representanteId() != null) {
            Representante representante = representanteRepository.findById(data.representanteId())
                    .orElseThrow(() -> new RuntimeException("Representante responsável não encontrado"));
            novoCliente.setRepresentante(representante);
        }
        return clienteRepository.save(novoCliente);
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
    @Transactional
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody ClienteCadastroDTO detalhesCliente) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(detalhesCliente.nome());
                    cliente.setSobrenome(detalhesCliente.sobrenome());
                    cliente.setCnpjCpf(detalhesCliente.cnpjCpf());
                    cliente.setEmail(detalhesCliente.email());
                    cliente.setTelefone(detalhesCliente.telefone());
                    cliente.setEndereco(detalhesCliente.endereco());
                    cliente.setResponsavel(detalhesCliente.responsavel());
                    cliente.setContatos(detalhesCliente.contatos());
                    cliente.setLatitude(detalhesCliente.latitude());
                    cliente.setLongitude(detalhesCliente.longitude());

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