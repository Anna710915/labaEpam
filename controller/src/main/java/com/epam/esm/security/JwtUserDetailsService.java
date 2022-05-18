package com.epam.esm.security;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Jwt user details service.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Instantiates a new Jwt user details service.
     *
     * @param userService the user service
     */
    @Autowired
    public JwtUserDetailsService(UserService userService){
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userService.findUserByName(username);
        return new JwtUser(userDto.getUserId(), userDto.getUsername(), userDto.getPassword(),
                List.of(new SimpleGrantedAuthority(userDto.getRole().name())));
    }
}
