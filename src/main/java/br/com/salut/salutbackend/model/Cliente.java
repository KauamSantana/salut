package br.com.salut.salutbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String cnpjCpf;
    private String email;
    private String telefone;
    private String endereco;
    private String responsavel;
    private String contatos;
    private Double latitude;
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "representante_id")
    @JsonIgnore // <-- ADICIONE O @JsonIgnore AQUI
    private Representante representante;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore // <-- TROQUE A ANOTAÇÃO AQUI
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore // <-- TROQUE A ANOTAÇÃO AQUI
    private List<Visita> visitas;


    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
    public String getCnpjCpf() { return cnpjCpf; }
    public void setCnpjCpf(String cnpjCpf) { this.cnpjCpf = cnpjCpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getContatos() { return contatos; }
    public void setContatos(String contatos) { this.contatos = contatos; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Representante getRepresentante() { return representante; }
    public void setRepresentante(Representante representante) { this.representante = representante; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public List<Visita> getVisitas() { return visitas; }
    public void setVisitas(List<Visita> visitas) { this.visitas = visitas; }
}