package com.epam.esm.controller;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.security.JwtUtil;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/certificates")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity login(@RequestBody UserDto requestDto){
        try{
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            UserDto userDto = userService.findUserByName(username);
            if (userDto == null){
                throw new UsernameNotFoundException("User with username: " + username + "not found");
            }
            String token = jwtUtil.createToken(username, List.of(userDto.getRole()));
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
