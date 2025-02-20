package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.User;
import com.project.bookstore.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testShouldFindUserByEmail() {
        String testEmail = "test@gmail.com";
        User testUser = new User();
        testUser.setEmail(testEmail);
        userRepository.save(testUser);

        boolean expected = userRepository.findByEmail(testEmail).isPresent();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testShouldNotFindUserByEmail() {
        String testEmail = "test@gmail.com";

        boolean expected=userRepository.findByEmail(testEmail).isPresent();

        Assertions.assertThat(expected).isFalse();
    }
}


