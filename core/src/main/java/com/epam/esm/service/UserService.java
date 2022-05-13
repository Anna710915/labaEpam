package com.epam.esm.service;


import com.epam.esm.model.dto.UserDto;

public interface UserService {

    UserDto findUserByName(String username);
}
