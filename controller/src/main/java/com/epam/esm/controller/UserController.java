package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.AuthenticationResponseDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.security.JwtUtil;
import com.epam.esm.service.UserService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping(value = "/certificates")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final MessageLanguageUtil messageLanguageUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Instantiates a new User controller.
     *
     * @param authenticationManager the authentication manager
     * @param jwtUtil               the jwt util
     * @param userService           the user service
     * @param passwordEncoder       the password encoder
     * @param messageLanguageUtil   the message language util
     */
    @Autowired
    public UserController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserService userService,
                          PasswordEncoder passwordEncoder,
                          MessageLanguageUtil messageLanguageUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    /**
     * Login response entity.
     *
     * @param requestDto the request dto
     * @return the response entity
     */
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto requestDto){
        try{
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            UserDto userDto = userService.findUserByName(username);
            if (userDto == null){
                throw new UsernameNotFoundException(messageLanguageUtil.getMessage("not_found.user") + username);
            }
            String token = jwtUtil.createToken(username, List.of(userDto.getRole()));
            return new ResponseEntity<>(new AuthenticationResponseDto(username, token), HttpStatus.OK);
        } catch (AuthenticationException e){
            throw new BadCredentialsException(messageLanguageUtil.getMessage("bad_request.invalid_username_or_password"), e);
        }
    }

    /**
     * Sign up response entity.
     *
     * @param builder the builder
     * @param userDto the user dto
     * @return the response entity
     */
    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<?> signUp(UriComponentsBuilder builder, @Valid @RequestBody UserDto userDto){
        String password = userDto.getPassword();
        userDto.setPassword(passwordEncoder.encode(password));
        UserDto newUser = userService.signUpUser(userDto);
        URI locationUri = builder.path("/certificates").path("/signup/").path(String.valueOf(newUser.getUserId()))
                .build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
