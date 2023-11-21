package com.falah.thefuturetest.entities.mappers;

import com.falah.thefuturetest.entities.User;
import com.falah.thefuturetest.entities.dtos.UserDto;
import com.falah.thefuturetest.entities.forms.UserForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromForm(UserForm user);

    UserDto toDto(User user);

    @Mapping(target = "authorities", ignore = true)
    User map(User user);
}
