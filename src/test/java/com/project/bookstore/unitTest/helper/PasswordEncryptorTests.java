package com.project.bookstore.unitTest.helper;

import com.project.bookstore.helper.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordEncryptorTests {

    @Test
    public void givenString_EncryptPasswordWithSHA256_ReturnEncryptedPassword() {
        String password = PasswordEncryptor.encryptPasswordWithSHA256("testPassword");

        Assertions.assertThat(password).isNotNull();
    }
}
