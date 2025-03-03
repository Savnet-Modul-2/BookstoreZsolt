package com.project.bookstore.validator;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.dto.BookWithExemplarsDto;
import com.project.bookstore.entity.BookExemplar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    @Autowired
    private BookExemplarValidator bookExemplarValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookDto.class.equals(clazz) || BookWithExemplarsDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.required", "isbn field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required", "title field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "author.required", "author field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appearanceDate", "appearanceDate.required", "appearanceDate field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nrOfPages", "nrOfPages.required", "nrOfPages field is required");
        if (target instanceof BookDto bookDto) {
            if (bookDto.getGenre() == null) {
                errors.rejectValue("genre", "genre.required", "genre field is required");
            }
            if (bookDto.getBookLanguage() == null) {
                errors.rejectValue("bookLanguage", "bookLanguage.required", "bookLanguage field is required");
            }
        } else if (target instanceof BookWithExemplarsDto bookDto) {
            bookExemplarValidator.validate(bookDto.getBookExemplar(), errors);
            if (bookDto.getGenre() == null) {
                errors.rejectValue("genre", "genre.required", "genre field is required");
            }
            if (bookDto.getBookLanguage() == null) {
                errors.rejectValue("bookLanguage", "bookLanguage.required", "bookLanguage field is required");
            }
        }
    }
}
