package com.falah.thefuturetest.entities.dtos;

import com.falah.thefuturetest.entities.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private User.UserRole role;
    private String token;

}
