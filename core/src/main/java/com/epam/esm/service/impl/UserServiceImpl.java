package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomNotValidArgumentException;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.entity.UserRole;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageLanguageUtil messageLanguageUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MessageLanguageUtil messageLanguageUtil) {
        this.userRepository = userRepository;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    @Override
    @Transactional
    public UserDto findUserByName(String username) {
        User user = userRepository.findByUsername(username);
        return buildUserDto(user);
    }

    @Override
    @Transactional
    public UserDto signUpUser(UserDto userDto) {
        User uniqueUser = userRepository.findByUsername(userDto.getUsername());
        checkUniqueUser(uniqueUser);
        User user = new User(userDto.getUsername(), userDto.getPassword(), UserRole.CLIENT);
        return buildUserDto(userRepository.save(user));
    }

    private UserDto buildUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private void checkUniqueUser(User uniqueUser){
        if(uniqueUser != null){
            throw new CustomNotValidArgumentException(messageLanguageUtil.getMessage("not_valid.not_uniq_username") + uniqueUser.getUsername());
        }
    }
}
