package com.project.bookstore.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorDetail {
    private LocalDateTime time;
    private String errorMessage;
    private Map<String, String> errorMap;

    public ErrorDetail(String errorMessage) {
        this.time = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    public ErrorDetail(Map<String, String> errorMap) {
        this.time = LocalDateTime.now();
        this.errorMap = errorMap;
    }


    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public void setErrorMap(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
