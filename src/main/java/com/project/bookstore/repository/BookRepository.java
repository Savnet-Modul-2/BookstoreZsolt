package com.project.bookstore.repository;

import com.project.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = """
            SELECT book FROM book book
                   WHERE (:author IS NULL OR book.author = :author OR book.author LIKE %:author%)
                   AND (:title IS NULL OR book.title = :title OR book.title LIKE %:title%)
            """)
    Page<Book> findBooks(String title, String author, Pageable pageable);
}

