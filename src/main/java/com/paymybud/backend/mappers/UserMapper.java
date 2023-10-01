package com.paymybud.backend.mappers;

import com.paymybud.backend.dto.SignUpDto;
import com.paymybud.backend.dto.UserDto;
import com.paymybud.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
