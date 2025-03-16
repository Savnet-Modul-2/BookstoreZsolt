package com.project.bookstore.validator;

import com.project.bookstore.dto.BookWithExemplarsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookWithExemplarsValidator implements Validator {
    @Autowired
    private BookExemplarsToCreateValidator bookExemplarsToCreateValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookWithExemplarsDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookWithExemplarsDto bookWithExemplarsDto = (BookWithExemplarsDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "isbn", "isbn.required", "isbn field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required", "title field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "author", "author.required", "author field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "appearanceDate", "appearanceDate.required", "appearanceDate field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nrOfPages", "nrOfPages.required", "nrOfPages field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "genre", "genre.required", "genre field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bookLanguage", "bookLanguage.required", "bookLanguage field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bookExemplar", "bookExemplar.required", "bookExemplar field is required");
        if (bookWithExemplarsDto.getBookExemplar() != null) {
            bookExemplarsToCreateValidator.validate(bookWithExemplarsDto.getBookExemplar(), errors);
        }
    }
}
