package com.epam.esm.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The type Jwt user.
 */
public class JwtUser implements UserDetails {

    private long userId;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Instantiates a new Jwt user.
     *
     * @param userId      the user id
     * @param username    the username
     * @param password    the password
     * @param authorities the authorities
     */
    public JwtUser(long userId, String username, String password, Collection<? extends GrantedAuthority> authorities){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Get user id long.
     *
     * @return the long
     */
    public long getUserId(){
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
