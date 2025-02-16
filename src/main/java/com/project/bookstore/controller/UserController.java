package com.project.bookstore.controller;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.exceptions.UserValidationException;
import com.project.bookstore.helper.UserValidator;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.service.EmailService;
import com.project.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private EmailService emailService;

    @InitBinder("userDto")
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(userValidator);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new UserValidationException(errorMap);
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


    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> verifyAccount(@PathVariable(name = "userId") Long id, @RequestBody Map<String, String> codeMap) throws CodeExpirationTimeException {
        User verifiedUser = userService.verifyUserCode(id, codeMap.get("verifiableCode"));
        return ResponseEntity.ok(verifiedUser);
    }
}
