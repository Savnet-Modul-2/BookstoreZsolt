package com.project.bookstore.helper;

import com.project.bookstore.exceptions.NoSuchAlgorithmWrapperException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {
    private PasswordEncryptor() {
    }

    public static String encryptUserPasswordWithSHA256(String userPassword) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = messageDigest.digest(userPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte hash : encodedHash) {
                String hex = Integer.toHexString(0xff & hash);
                if (encodedHash.length == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmWrapperException("Unable to encrypt password", e);
        }
    }
}
