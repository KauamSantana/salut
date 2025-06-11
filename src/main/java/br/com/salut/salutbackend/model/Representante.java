package br.com.salut.salutbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    private String sobrenome;
    private String email;
    private String telefone;
    private String regiao;
    private String status;
    private String senha;
    private BigDecimal meta;
    private BigDecimal taxaComissao;
    private String role;

    @OneToMany(mappedBy = "representante")
    @JsonManagedReference("representante-clientes")
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "representante")
    @JsonManagedReference("representante-pedidos")
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "representante")
    @JsonManagedReference("representante-visitas")
    private List<Visita> visitas;


    // Getters e Setters
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
    public List<Cliente> getClientes() { return clientes; }
    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public List<Visita> getVisitas() { return visitas; }
    public void setVisitas(List<Visita> visitas) { this.visitas = visitas; }


    // Métodos UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) return List.of();
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