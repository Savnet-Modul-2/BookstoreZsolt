package com.project.bookstore.mapper;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.helper.PasswordEncryptor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User mapUserDtoToUser(UserDto userDto) throws NoSuchAlgorithmException {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setYearOfBirth(userDto.getYearOfBirth());
        user.setGender(userDto.getGender());
        user.setEmail(userDto.getEmail());
        user.setPassword(PasswordEncryptor.encryptUserPasswordWithSHA256(userDto.getPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());
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

    @SneakyThrows
    public List<User> mapUserDtoListToUserList(List<UserDto> userDtoList) {
        List<User>userList=new ArrayList<>();
        for(UserDto userDto:userDtoList){
            userList.add(mapUserDtoToUser(userDto));
        }
        return userList;
    }

    public List<UserDto> mapUserListToUserDtoList(List<User> userList) {
        return userList.stream().map(this::mapUserToUserDto).toList();
    }



}
