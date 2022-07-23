package com.epam.esm.dto;

import com.epam.esm.model.entity.UserRole;

/**
 * The type Authentication response dto.
 */
public class AuthenticationResponseDto {

    private String username;
    private String token;
    private UserRole role;

    /**
     * Instantiates a new Authentication response dto.
     */
    public AuthenticationResponseDto(){}

    /**
     * Instantiates a new Authentication response dto.
     *
     * @param username the username
     * @param token    the token
     */
    public AuthenticationResponseDto(String username, String token, UserRole role) {
        this.username = username;
        this.token = token;
        this.role = role;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationResponseDto that = (AuthenticationResponseDto) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthenticationResponseDto{");
        sb.append("username='").append(username).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}

