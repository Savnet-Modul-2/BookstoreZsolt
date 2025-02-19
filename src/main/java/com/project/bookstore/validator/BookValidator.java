package com.project.bookstore.validator;

import com.project.bookstore.dto.BookDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDto bookDto = (BookDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.required", "isbn field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required", "title field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "author.required", "author field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appearanceDate", "appearanceDate.required", "appearanceDate field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nrOfPages", "nrOfPages.required", "nrOfPages field is required");
        if (bookDto.getGenre() == null) {
            errors.rejectValue("genre", "genre.required", "genre field is required");
        }
        if (bookDto.getBookLanguage() == null) {
            errors.rejectValue("bookLanguage", "bookLanguage.required", "bookLanguage field is required");
        }
    }
}
