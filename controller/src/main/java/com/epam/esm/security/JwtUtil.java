package com.epam.esm.security;

import com.epam.esm.model.entity.UserRole;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * The type Jwt util.
 */
@Component
public class JwtUtil {

    private static final String ROLE = "role";
    private static final String AUTHORISATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Value("${secret}")
    private String secret;

    @Value("${validity_in_milliseconds}")
    private long validityMillisecond;

    private final UserDetailsService userDetailsService;

    /**
     * Instantiates a new Jwt util.
     *
     * @param userDetailsService the user details service
     */
    @Autowired
    public JwtUtil(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    /**
     * Create token string.
     *
     * @param username the username
     * @param roles    the roles
     * @return the string
     */
    public String createToken(String username, List<UserRole> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLE, roles);
        Date dateNow = new Date();
        Date validity = new Date(dateNow.getTime() + validityMillisecond);
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(dateNow)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * Get authentication authentication.
     *
     * @param token the token
     * @return the authentication
     */
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Get username string.
     *
     * @param token the token
     * @return the string
     */
    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validate token boolean.
     *
     * @param token the token
     * @return the boolean
     */
    public boolean validateToken(String token){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException("Invalid token " + token,  e);
        }
    }

    /**
     * Resolve token string.
     *
     * @param request the request
     * @return the string
     */
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORISATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)){
            return bearerToken.substring(7);
        }
        return null;
    }

}
