package com.project.bookstore.controller;

import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.exceptions.RequestBodyMapKeyNotFoundException;
import com.project.bookstore.validator.UserValidator;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.service.EmailService;
import com.project.bookstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        User createdUser = userService.createUser(userMapper.mapUserFromUserDto(userDto));
        return ResponseEntity.ok(userMapper.mapUserDtoFromUser(createdUser));
    }

    @GetMapping()
    public ResponseEntity<?> findAllUsers() {
        List<User> allUserList = userService.findAll();
        return ResponseEntity.ok(userMapper.mapUserDtoListFromUserList(allUserList));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable(name = "userId") Long userId) {
        User foundUser = userService.findById(userId);
        return ResponseEntity.ok(userMapper.mapUserDtoFromUser(foundUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> verifyAccount(@PathVariable(name = "userId") Long id,
                                           @RequestBody Map<String, String> codeMap) {
        if (!codeMap.containsKey("verificationCode")) {
            throw new RequestBodyMapKeyNotFoundException("Missing verificationCode property");
        }
        User verifiedUser = userService.verifyUserCode(id, codeMap.get("verificationCode"));
        return ResponseEntity.ok(userMapper.mapUserDtoFromUser(verifiedUser));
    }

    @PutMapping("/login")
    public ResponseEntity<?> loginIntoAccount(@RequestBody Map<String, String> loginCredentials) {
        if (!loginCredentials.containsKey("email") || !loginCredentials.containsKey("password")) {
            throw new RequestBodyMapKeyNotFoundException("Missing email and/or password properties");
        }
        Long id = userService.getUserIdAfterLogin(loginCredentials.get("email"), loginCredentials.get("password"));
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
