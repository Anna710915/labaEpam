package com.epam.esm.service;


import com.epam.esm.model.dto.UserDto;

/**
 * The interface User service.
 */
public interface UserService{
    /**
     * Find user by name user dto.
     *
     * @param username the username
     * @return the user dto
     */
    UserDto findUserByName(String username);

    /**
     * Sign up user user dto.
     *
     * @param userDto the user dto
     * @return the user dto
     */
    UserDto signUpUser(UserDto userDto);
}
