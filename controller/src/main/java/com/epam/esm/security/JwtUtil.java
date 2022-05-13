package com.epam.esm.security;

import com.epam.esm.model.entity.UserRole;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private String secret = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtUtil(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username, List<UserRole> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", roles);
        Date dateNow = new Date();
        Date validity = new Date(dateNow.getTime() + VALIDITY_IN_MILLISECONDS);
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(dateNow)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException("Invalid token " + token,  e);
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

}
