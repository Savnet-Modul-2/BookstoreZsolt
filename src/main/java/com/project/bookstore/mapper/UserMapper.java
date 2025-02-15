package com.project.bookstore.mapper;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public User mapUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setYearOfBirth(userDto.getYearOfBirth());
        user.setGender(userDto.getGender());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        user.setCountry(userDto.getCountry());
        user.setVerifiedAccount(userDto.isVerifiedAccount());
        return user;
    }

    public UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setYearOfBirth(user.getYearOfBirth());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setPassword(user.getPassword());
        userDto.setCountry(user.getCountry());
        userDto.setVerifiedAccount(userDto.isVerifiedAccount());
        return userDto;
    }

    public List<User> mapUserDtoListToUserList(List<UserDto> userDtoList) {
        return userDtoList.stream().map(this::mapUserDtoToUser).toList();
    }

    public List<UserDto> mapUserListToUserDtoList(List<User> userList) {
        return userList.stream().map(this::mapUserToUserDto).toList();
    }


}
