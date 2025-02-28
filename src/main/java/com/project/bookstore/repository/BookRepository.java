package com.project.bookstore.repository;

import com.project.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    //TODO: implement test
    @Query(value = """
            SELECT book FROM book b
            WHERE(:author IS NULL OR b.author=:author OR b.author LIKE ':author%')
            AND (:title IS NULL OR  b.title=:title OR b.title LIKE ':title%')
            """)
    List<Book> findBook(String title, String author);
}
