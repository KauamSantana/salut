package br.com.salut.salutbackend.service;

import br.com.salut.salutbackend.model.Representante;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Esta é a chave secreta que será usada para assinar o token.
    // Em um projeto real, ela estaria no application.properties para ser mais segura.
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Representante representante) {
        try {
            // Algoritmo de assinatura usando nossa chave secreta
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Salut.Vinhos") // Quem está emitindo o token
                    .withSubject(representante.getEmail()) // O "dono" do token (o usuário)
                    .withExpiresAt(gerarDataExpiracao()) // Define a validade do token
                    .sign(algoritmo); // Assina o token para garantir sua integridade
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    // Método auxiliar para extrair o usuário do token (usaremos depois)
    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Salut.Vinhos")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant gerarDataExpiracao() {
        // Define que o token vai expirar em 2 horas a partir de agora
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}