package com.falah.thefuturetest.controllers;

import com.falah.thefuturetest.entities.User;
import com.falah.thefuturetest.entities.dtos.UserDto;
import com.falah.thefuturetest.entities.forms.LoginForm;
import com.falah.thefuturetest.entities.forms.UserForm;
import com.falah.thefuturetest.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserForm user) {
        return userService.createUser(user);
    }

    //    @PreAuthorize("hasAuthority('string')")
    @GetMapping
    @PageableAsQueryParam
    public Page<UserDto> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
        return userService.getAllUsers(pageable);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("current")
    public UserDto getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return userService.getUser(currentUser.getId());
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginDto) {
        return new ResponseEntity<>(userService.login(loginDto).getToken(), HttpStatus.OK);
    }

}