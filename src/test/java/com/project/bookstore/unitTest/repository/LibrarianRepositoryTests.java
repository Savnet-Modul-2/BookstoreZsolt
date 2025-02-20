package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.repository.LibrarianRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class LibrarianRepositoryTests {

    @Autowired
    private LibrarianRepository librarianRepository;

    @AfterEach
    void tearDown(){
        librarianRepository.deleteAll();
    }

    @Test
    public void testShouldFindLibrarianById(){
        String testEmail="test@gmail.com";
        Librarian testLibrarian=new Librarian();
        testLibrarian.setEmail(testEmail);
        librarianRepository.save(testLibrarian);

        boolean expected=librarianRepository.findByEmail(testEmail).isPresent();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testShouldNotFindUserByEmail() {
        String testEmail = "test@gmail.com";

        boolean expected=librarianRepository.findByEmail(testEmail).isPresent();

        Assertions.assertThat(expected).isFalse();
    }


}
