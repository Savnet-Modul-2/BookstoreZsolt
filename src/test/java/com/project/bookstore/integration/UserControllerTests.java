package com.project.bookstore.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.UserDto;
import com.project.bookstore.entity.User;
import com.project.bookstore.entity.types.Gender;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.helper.PasswordEncryptor;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    private UserDto testUser;

    @BeforeEach
    public void setUp() {
        testUser = new UserDto();
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("testEmail@gmail.com");
        testUser.setPassword("testPassword");
        testUser.setPhoneNumber("+40123456789");
        testUser.setCountry("testCountry");
        testUser.setGender(Gender.MALE);
        testUser.setYearOfBirth(2000);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void givenUserDto_CreateUser_ReturnOk() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()));
    }

    @Test
    public void givenEmptyField_CreateUser_ReturnBadRequest() throws Exception {
        testUser.setFirstName("");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMap").exists())
                .andExpect(jsonPath("$.errorMap.firstName").value("firstName field is required"));
    }

    @Test
    public void givenWrongPhoneNumberFormat_CreateUser_ReturnBadRequest() throws Exception {
        testUser.setPhoneNumber("+4049162");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMap").exists())
                .andExpect(jsonPath("$.errorMap.phoneNumber").value("Invalid phone number format"));
    }

    @Test
    public void givenWrongEmailFormat_CreateUser_ReturnBadRequest() throws Exception {
        testUser.setEmail("testEmail");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMap").exists())
                .andExpect(jsonPath("$.errorMap.email").value("Invalid email format"));
    }

    @Test
    public void givenExistingEmail_CreateUser_ReturnConflict() throws Exception {
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("User with the email address %s already exists".formatted(testUser.getEmail())));
    }

    @Test
    public void givenNothing_GetAllUsers_ReturnStatusOk() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenUserId_FindUserById_ReturnOk() throws Exception {
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));

        mockMvc.perform(get("/users/%s".formatted(savedUser.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.firstName").value(savedUser.getFirstName()));
    }

    @Test
    public void givenNothing_FindUserById_ReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/%s".formatted(1L)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("User with id %s not found".formatted(1L)));
    }

    @Test
    public void givenVerificationCode_VerifyAccount_ReturnOk() throws Exception {
        testUser.setVerificationCode(CodeGenerator.generateCode());
        testUser.setVerificationCodeTime(LocalDateTime.now());
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> codeMap = Map.of("verificationCode", savedUser.getVerificationCode());

        mockMvc.perform(put("/users/%s".formatted(savedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verifiedAccount").value(true));
    }

    @Test
    public void givenWrongMapKey_VerifyAccount_ReturnBadRequest() throws Exception {
        testUser.setVerificationCode(CodeGenerator.generateCode());
        testUser.setVerificationCodeTime(LocalDateTime.now());
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> codeMap = Map.of("code", savedUser.getVerificationCode());

        mockMvc.perform(put("/users/%s".formatted(savedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("Missing verificationCode property"));
    }

    @Test
    public void givenWrongId_VerifyAccount_ReturnNotFound() throws Exception {
        testUser.setVerificationCode(CodeGenerator.generateCode());
        testUser.setVerificationCodeTime(LocalDateTime.now());
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> codeMap = Map.of("verificationCode", savedUser.getVerificationCode());

        mockMvc.perform(put("/users/%s".formatted(2L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeMap)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("User with id %s not found".formatted(2L)));
    }

    @Test
    public void givenWrongTime_VerifyAccount_ReturnBadRequest() throws Exception {
        testUser.setVerificationCode(CodeGenerator.generateCode());
        testUser.setVerificationCodeTime(LocalDateTime.now().minusHours(2));
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> codeMap = Map.of("verificationCode", savedUser.getVerificationCode());

        mockMvc.perform(put("/users/%s".formatted(savedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("The time for code verification has expired"));
    }

    @Test
    public void givenUserEmailAndPassword_LoginIntoAccount_ReturnOk() throws Exception {
        testUser.setVerifiedAccount(true);
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> loginMap = Map.of("email", testUser.getEmail(), "password", testUser.getPassword());

        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(savedUser.getId()));
    }

    @Test
    public void givenWrongMapKey_LoginIntoAccount_ReturnBadRequest() throws Exception {
        Map<String, String> loginMap = Map.of("userEmail", testUser.getEmail(), "userPassword", testUser.getPassword());

        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("Missing email and/or password properties"));
    }

    @Test
    public void givenWrongEmail_LoginIntoAccount_ReturnNotFound() throws Exception {
        Map<String, String> loginMap = Map.of("email", testUser.getEmail(), "password", testUser.getPassword());

        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginMap)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("User with email %s not found".formatted(testUser.getEmail())));
    }

    @Test
    public void givenUserNotVerified_LoginIntoAccount_ReturnBadRequest() throws Exception {
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> loginMap = Map.of("email", testUser.getEmail(), "password", testUser.getPassword());

        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("This account is not yet verified"));
    }

    @Test
    public void givenWrongPassword_LoginIntoAccount_ReturnBadRequest() throws Exception {
        testUser.setVerifiedAccount(true);
        String newTestPassword = "newTestPassword";
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));
        Map<String, String> loginMap = Map.of("email", testUser.getEmail(), "password", newTestPassword);

        mockMvc.perform(put("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginMap)))
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("Couldn't login to the account with the provided password"));
    }

    @Test
    public void givenUserEmail_ResendVerificationCodeEmail_ReturnOk() throws Exception {
        Map<String, String> emailMap = Map.of("userEmail", testUser.getEmail());
        testUser.setVerificationCode(CodeGenerator.generateCode());
        testUser.setVerificationCodeTime(LocalDateTime.now());
        userRepository.save(userMapper.mapUserFromUserDto(testUser));

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(EmailDetails.EMAIL_SENT_SUCCESSFULLY));
    }

    @Test
    public void givenWrongMapKey_ResendVerificationCodeEmail_ReturnBadRequest() throws Exception {
        Map<String, String> emailMap = Map.of("email", testUser.getEmail());

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("Missing userEmail property"));
    }

    @Test
    public void givenWrongEmail_ResendVerificationCodeEmail_ReturnNotFound() throws Exception {
        Map<String, String> emailMap = Map.of("userEmail", testUser.getEmail());
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailMap)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andExpect(jsonPath("$.errorMessage").value("User with email %s not found".formatted(testUser.getEmail())));
    }

    @Test
    public void givenUserId_DeleteUserById_ReturnNoContent() throws Exception {
        User savedUser = userRepository.save(userMapper.mapUserFromUserDto(testUser));

        mockMvc.perform(delete("/users/%s".formatted(savedUser.getId())))
                .andExpect(status().isNoContent());
    }
}
