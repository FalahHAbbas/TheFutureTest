package com.falah.thefuturetest.entities.forms;

import com.falah.thefuturetest.entities.User;
import lombok.Data;

@Data
public class UserForm {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private User.UserRole role;
}
