package br.com.salut.salutbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity
public class Representante implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome; // NOVO CAMPO
    private String email;
    private String telefone; // NOVO CAMPO
    private String regiao; // NOVO CAMPO
    private String status; // NOVO CAMPO
    private String senha;
    private BigDecimal meta;
    private BigDecimal taxaComissao;
    private String role; // NOVO CAMPO PARA DIFERENCIAR ADMIN/REPRESENTANTE

    // Getters e Setters ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public BigDecimal getMeta() { return meta; }
    public void setMeta(BigDecimal meta) { this.meta = meta; }
    public BigDecimal getTaxaComissao() { return taxaComissao; }
    public void setTaxaComissao(BigDecimal taxaComissao) { this.taxaComissao = taxaComissao; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Métodos UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Agora a permissão é baseada no campo "role"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.toUpperCase()));
    }
    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}