package com.epam.esm.dto;

/**
 * The type Authentication response dto.
 */
public class AuthenticationResponseDto {

    private String username;
    private String token;

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
    public AuthenticationResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationResponseDto that = (AuthenticationResponseDto) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthenticationResponseDto{");
        sb.append("username='").append(username).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
