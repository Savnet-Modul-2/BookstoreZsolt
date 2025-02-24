package com.project.bookstore.repository;

import com.project.bookstore.entity.BookExemplary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookExemplaryRepository extends JpaRepository<BookExemplary, Long> {
}
