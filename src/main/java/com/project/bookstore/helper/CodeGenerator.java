package com.project.bookstore.helper;

import java.util.Random;

public class CodeGenerator {
    private CodeGenerator() {
    }

    public static String generateCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(1000, 9999));
    }
}
