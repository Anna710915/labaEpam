package com.epam.esm.dto;

/**
 * The type Authentication request dto.
 */
public class AuthenticationRequestDto {

    private String username;
    private String password;

    /**
     * Instantiates a new Authentication request dto.
     */
    public AuthenticationRequestDto(){}

    /**
     * Instantiates a new Authentication request dto.
     *
     * @param login    the login
     * @param password the password
     */
    public AuthenticationRequestDto(String login, String password) {
        this.username = login;
        this.password = password;
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
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
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
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationRequestDto that = (AuthenticationRequestDto) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthenticationRequestDto{" + "login='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
