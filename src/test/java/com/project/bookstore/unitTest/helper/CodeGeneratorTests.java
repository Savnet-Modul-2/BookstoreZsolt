package com.project.bookstore.unitTest.helper;

import com.project.bookstore.helper.CodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CodeGeneratorTests {

    @Test
    public void givenNothing_GenerateCode_ReturnCode() {
        String code = CodeGenerator.generateCode();

        Assertions.assertThat(code).isNotNull();
    }
}
