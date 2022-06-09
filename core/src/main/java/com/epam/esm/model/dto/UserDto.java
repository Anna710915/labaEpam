package com.epam.esm.model.dto;

import com.epam.esm.model.entity.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    private long userId;

    @NotBlank(message = "{not_blank_username}")
    @Pattern(regexp = "[A-zА-я0-9-_\\.]+", message = "{username_validation_symbols}")
    @Size(min = 4, max = 50, message = "{size_username}")
    private String username;

    @NotBlank(message = "{not_blank_password}")
    @Pattern(regexp = "[A-zА-я0-9-_]+", message = "{password_validation_symbols}")
    @Size(min = 4, max = 50, message = "{size_password}")
    private String password;
    private UserRole role;

    public UserDto(){}

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(long userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public UserDto(long userId, String username, String password, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

        UserDto userDto = (UserDto) o;

        if (userId != userDto.userId) return false;
        if (username != null ? !username.equals(userDto.username) : userDto.username != null) return false;
        if (password != null ? !password.equals(userDto.password) : userDto.password != null) return false;
        return role == userDto.role;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" + "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
