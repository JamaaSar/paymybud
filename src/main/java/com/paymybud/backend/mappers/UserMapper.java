package com.paymybud.backend.mappers;

import com.paymybud.backend.dto.FriendDTO;
import com.paymybud.backend.dto.SignUpDTO;
import com.paymybud.backend.dto.UserDTO;
import com.paymybud.backend.entity.User;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Generated
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);
    User friendDtoToUser(FriendDTO friendDTO);
    List<FriendDTO> toFriendDto(List<User> userList);
    List<UserDTO> toUserDtoList(List<User> userList);
    User signUpToUser(SignUpDTO signUpDto);
    User userToFriend(User user);

}
