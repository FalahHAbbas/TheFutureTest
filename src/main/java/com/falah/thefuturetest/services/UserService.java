package com.falah.thefuturetest.services;

import com.falah.thefuturetest.entities.User;
import com.falah.thefuturetest.entities.dtos.UserDto;
import com.falah.thefuturetest.entities.forms.LoginForm;
import com.falah.thefuturetest.entities.forms.UserForm;
import com.falah.thefuturetest.entities.mappers.UserMapper;
import com.falah.thefuturetest.repositories.UserRepository;
import com.falah.thefuturetest.config.securety.SecurityConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserForm userForm) {
        User userEntity = mapper.fromForm(userForm);
        var isUserExist = userRepository.findFirstByEmail(userForm.getEmail());
        if (isUserExist.isPresent()) {
            throw new RuntimeException("User already exist");
        }
        userEntity = userRepository.save(userEntity);

        var user = mapper.toDto(userEntity);
        Map<String, Object> claims = new HashMap<>();
        Map<String, Object> userInfo =
                new ObjectMapper().convertValue(userEntity, new TypeReference<>() {
                });
        claims.put("user", userInfo);
        user.setToken(Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()))
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(userEntity.getEmail())
//          .setSubject(new ObjectMapper().writeValueAsString(userResult))
                .addClaims(claims)
                .setExpiration(Date.from(
                        LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .claim("rol", userEntity.getRole())
                .compact());
        return user;
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return users.map(mapper::toDto);
    }


    public UserDto getUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        var dto = mapper.toDto(user);
        return dto;
    }

    public UserDto login(LoginForm userForm) {
        var userResult = userRepository.findFirstByEmail(userForm.getUsername());
        if (userResult.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!userResult.get().getPassword().equals(userForm.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        Map<String, Object> claims = new HashMap<>();
        userResult.get().setPassword(null);
        Map<String, Object> userInfo =
                new ObjectMapper().convertValue(userResult.get(), new TypeReference<>() {
                });
        claims.put("user", userInfo);

        var user = mapper.toDto(userResult.get());
        user.setToken(Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes()))
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(userResult.get().getEmail())
                .addClaims(claims)
                .setExpiration(Date.from(
                        LocalDate.now().plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .claim("rol", userResult.get().getRole())
                .compact());
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

}
