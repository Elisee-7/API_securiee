package com.exemple.eventapi.security;

import com.exemple.eventapi.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

            System.out.println(">>> JwtAuthenticationFilter déclenché pour " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        System.out.println(">>> Header Authorization: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>> Pas de Bearer Token, on continue sans authentification");
            filterChain.doFilter(request, response);
            return;
        }

       String token = authHeader.substring(7);
    try {
        Jws<Claims> claims = jwtService.parseToken(token);
        String username = claims.getBody().getSubject();
        String role = claims.getBody().get("role", String.class);

        System.out.println("Token subject: " + username);
        System.out.println("Token role: " + role);


        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );
            System.out.println("Authorities: " + authentication.getAuthorities());


        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(">>> SecurityContext après injection: " + SecurityContextHolder.getContext().getAuthentication());

        System.out.println(">>> SecurityContext mis à jour !");
    } catch (JwtException e) {
    System.out.println(">>> Erreur JWT: " + e.getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
    return;
    }


        filterChain.doFilter(request, response);
        System.out.println(">>> Fin du filtre JWT");
    }
    
}
