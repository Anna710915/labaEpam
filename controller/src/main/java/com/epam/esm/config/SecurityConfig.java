package com.epam.esm.config;

import com.epam.esm.model.entity.UserRole;
import com.epam.esm.permission.UserPermission;
import com.epam.esm.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtTokenFilter jwtTokenFilter){
        this.userDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        EnumSet<UserPermission> userPermissions = EnumSet.allOf(UserPermission.class);
        addUsersPermissions(http, userPermissions);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private void addUsersPermissions(HttpSecurity http, EnumSet<UserPermission> userPermissions) throws Exception {
        for(UserPermission userPermission : userPermissions){
            addPermissions(http, userPermission);
        }
    }

    private void addPermissions(HttpSecurity httpSecurity, UserPermission userPermission) throws Exception {
        Map<HttpMethod, String[]> permissions = userPermission.getPermissions();
        Set<HttpMethod> methodTypes = permissions.keySet();
        for(HttpMethod method : methodTypes){
            switch (userPermission){
                case ADMIN -> httpSecurity.authorizeRequests().antMatchers(method, permissions.get(method)).hasAnyAuthority(UserRole.ADMIN.name());
                case CLIENT -> httpSecurity.authorizeRequests().antMatchers(method, permissions.get(method)).hasAnyAuthority(UserRole.ADMIN.name(), UserRole.CLIENT.name());
                case GUEST -> httpSecurity.authorizeRequests().antMatchers(method, permissions.get(method)).permitAll();
                default -> throw new IllegalArgumentException("Not such user role" + userPermission);
            }
        }
    }
}
