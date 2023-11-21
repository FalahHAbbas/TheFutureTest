package com.falah.thefuturetest.controllers;

import com.falah.thefuturetest.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check")
public class CheckController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String user() {
        return "Hello User";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/adminOrUser")
    public String adminOrUser(@AuthenticationPrincipal User currentUser) {
        var role = currentUser.getRole();
        return "Hello " + role;
    }

}