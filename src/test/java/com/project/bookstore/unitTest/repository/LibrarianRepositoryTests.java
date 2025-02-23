package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.repository.LibrarianRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private Librarian testLibrarian;

    @BeforeEach
    public void setUp() {
        testLibrarian = new Librarian();
        testLibrarian.setEmail("test@gmail.com");
    }

    @AfterEach
    public void tearDown() {
        librarianRepository.deleteAll();
    }

    @Test
    public void testShouldFindLibrarianByEmailIfExists() {
        String testEmail = "test@gmail.com";

        librarianRepository.save(testLibrarian);
        boolean expected = librarianRepository.existsByEmail(testEmail);

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testShouldNotFindLibrarianByEmailNotExists() {
        String testEmail = "test@gmail.com";

        boolean expected = librarianRepository.existsByEmail(testEmail);

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testShouldFindLibrarianByEmail() {
        String testEmail = "test@gmail.com";

        librarianRepository.save(testLibrarian);
        Librarian foundLibrarian = librarianRepository.findByEmail(testEmail).orElseThrow();

        Assertions.assertThat(foundLibrarian).isEqualTo(testLibrarian);
    }

    @Test
    public void testShouldNotFindLibrarianByEmail() {
        String testEmail = "test@gmail.com";

        Librarian foundLibrarian = librarianRepository.findByEmail(testEmail).orElse(null);

        Assertions.assertThat(foundLibrarian).isNotEqualTo(testLibrarian);
    }
}
