package com.project.bookstore.repository;

import com.project.bookstore.entity.BookExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface BookExemplarRepository extends JpaRepository<BookExemplar, Long> {
    @Query(value = """
            SELECT * FROM book_exemplar bookExemplar
            WHERE bookExemplar.book_id = :bookId
            AND bookExemplar.id NOT IN (
            SELECT reservation.reserved_exemplar_id from reservation reservation
            WHERE reservation.reserved_exemplar_id = bookExemplar.id
            AND NOT (reservation.end_date < :userStartDate OR reservation.start_date > :userEndDate))
            LIMIT 1
            """, nativeQuery = true)
    Optional<BookExemplar> findFirstExemplarAvailable(Long bookId, LocalDate userStartDate, LocalDate userEndDate);
}
