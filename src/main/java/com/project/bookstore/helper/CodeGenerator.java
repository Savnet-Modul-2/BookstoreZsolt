package com.project.bookstore.helper;

import java.util.Random;

public class CodeGenerator {
    public static final String CODE_EMAIL_STRING="Your verification code for your account is: %s" +
            "\n This code is valid for 1 hour.";

    public static String generateCode(){
        Random random=new Random();
        return String.valueOf(random.nextInt(1000, 9999));
    }
}
