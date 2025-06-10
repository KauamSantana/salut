package br.com.salut.salutbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int safra;
    private String tipo;
    private String notasDeDegustacao;
    private String harmonizacoes;
    private String imagemUrl;

    // Dentro da classe Vinho
    private java.math.BigDecimal precoUnitario;

    // ... e o getter/setter
    public java.math.BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(java.math.BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getSafra() { return safra; }
    public void setSafra(int safra) { this.safra = safra; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getNotasDeDegustacao() { return notasDeDegustacao; }
    public void setNotasDeDegustacao(String notasDeDegustacao) { this.notasDeDegustacao = notasDeDegustacao; }
    public String getHarmonizacoes() { return harmonizacoes; }
    public void setHarmonizacoes(String harmonizacoes) { this.harmonizacoes = harmonizacoes; }
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
}