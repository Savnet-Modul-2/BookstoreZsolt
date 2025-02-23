package com.project.bookstore.mapper;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.helper.PasswordEncryptor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User mapUserFromUserDto(UserDto userDto) throws NoSuchAlgorithmException {
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
        user.setVerificationCode(userDto.getVerificationCode());
        user.setVerificationCodeTime(userDto.getVerificationCodeTime());
        return user;
    }

    public UserDto mapUserDtoFromUser(User user) {
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
        userDto.setVerifiedAccount(user.isVerifiedAccount());
        userDto.setVerificationCode(user.getVerificationCode());
        userDto.setVerificationCodeTime(user.getVerificationCodeTime());
        return userDto;
    }

    //TODO: check again for NoSuchAlgorithmException
    public List<User> mapUserListFromUserDtoList(List<UserDto> userDtoList) throws NoSuchAlgorithmException {
        List<User> userList = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            userList.add(mapUserFromUserDto(userDto));
        }
        return userList;
    }

    public List<UserDto> mapUserDtoListFromUserList(List<User> userList) {
        return userList.stream().map(this::mapUserDtoFromUser).toList();
    }
}
