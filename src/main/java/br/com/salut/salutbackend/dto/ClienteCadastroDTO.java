package br.com.salut.salutbackend.dto;

// DTO para receber dados para criar/atualizar um cliente
public record ClienteCadastroDTO(
        String nome,
        String sobrenome,
        String cnpjCpf,
        String email,
        String telefone,
        String endereco,
        String responsavel,
        String contatos,
        Double latitude,
        Double longitude,
        Long representanteId // Apenas o ID do representante
) {
}