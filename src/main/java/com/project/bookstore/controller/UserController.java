package com.project.bookstore.controller;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.helper.UserValidator;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(userValidator);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(bindingResult.getAllErrors().toString());
        }
        User createdUser = userService.createUser(userMapper.mapUserDtoToUser(userDto));
        return ResponseEntity.ok(userMapper.mapUserToUserDto(createdUser));
    }

    @GetMapping()
    public ResponseEntity<?> findAllUsers() {
        List<User> allUserList = userService.findAllUsers();
        return ResponseEntity.ok(userMapper.mapUserListToUserDtoList(allUserList));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable(name = "userId") Long userId) {
        User foundUser = userService.findUserById(userId);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(foundUser));
    }

/*    @PutMapping("/{userId}")
    public ResponseEntity<?>updateUserFoundById(@PathVariable(name="userId")Long userId,@RequestBody UserDto userDto){
        User updatedUser=userService.updateUserFoundById(userId,userDto);
        return ResponseEntity.ok(userMapper.mapUserToUserDto(updatedUser))
    }*/

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
