package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookExemplarRepositoryTests {
    @Autowired
    private BookExemplarRepository bookExemplarRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    private BookExemplar testBookExemplar;
    private User testUser;
    private Reservation testReservation;
    private Book testBook;


    @BeforeEach
    public void setUp() {
        testBookExemplar = new BookExemplar();
        testUser = new User();
        testReservation = new Reservation();
        testBook = new Book();
        testBook.setBookExemplars(List.of(testBookExemplar));
    }

    @AfterEach
    public void tearDown() {
        bookExemplarRepository.deleteAll();
        userRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Test
    public void testFindFirstExemplarAvailable() {
        bookRepository.save(testBook);
        userRepository.save(testUser);

        boolean expected = bookExemplarRepository.findFirstExemplarAvailable(1L,
                LocalDate.of(2025, 3, 5),
                LocalDate.of(2025, 3, 7))
                .isPresent();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testFindFirstExemplarAvailableWhenBookIdDoesntExist(){

    }
}
