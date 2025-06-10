package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // NOVO IMPORT
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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

    // Modifique o método de busca por ID para ficar assim:
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id, Authentication authentication) {
        // Nosso "olho mágico"
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("==================================================");
            System.out.println("TESTE DE AUTENTICAÇÃO: Usuário chegou no controller!");
            System.out.println("Principal (Usuário): " + authentication.getName());
            System.out.println("Permissões: " + authentication.getAuthorities());
            System.out.println("==================================================");
        } else {
            System.out.println("==================================================");
            System.out.println("TESTE DE AUTENTICAÇÃO: Requisição chegou SEM usuário autenticado.");
            System.out.println("==================================================");
        }

        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
}