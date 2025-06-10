package br.com.salut.salutbackend.config;

import br.com.salut.salutbackend.repository.RepresentanteRepository;
import br.com.salut.salutbackend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RepresentanteRepository representanteRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT); // Valida o token e pega o email
            var representante = representanteRepository.findByEmail(subject);

            if (representante != null) {
                // Se o token for válido e o usuário existir, nós o autenticamos na requisição
                var authentication = new UsernamePasswordAuthenticationToken(representante, null, representante.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // O token vem depois do prefixo "Bearer ", então o removemos
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}