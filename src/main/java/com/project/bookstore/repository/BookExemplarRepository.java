package com.project.bookstore.repository;

import com.project.bookstore.entity.BookExemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookExemplarRepository extends JpaRepository<BookExemplar, Long> {
}
