package com.project.bookstore.helper;

public class EmailDetails {
    public static final String EMAIL_SENT_SUCCESSFULLY = "Your email with the code was sent successfully! Check your inbox";
    public static final String CODE_EMAIL_SUBJECT = "Your code for the Bookstore App";
    public static final String CODE_EMAIL_BODY = "Your verification code for your account is: %s\n This code is valid for 1 hour.";
    public static final String RESERVATION_CONFIRMATION_EMAIL_BODY = "Your reservation for %s was successful!\nYou can come and pick up your book at %s, %s starting from %s";
    public static final String RESERVATION_CONFIRMATION_EMAIL_SUBJECT = "Book reservation completed";
    public static final String RESERVATION_DELAYED_EMAIL_BODY = "Your reservation for %s has ended and you should return the book to %s, %s";
    public static final String RESERVATION_DELAYED_EMAIL_SUBJECT = "Book return delayed";
    public static final String BOOK_DELAYED_EMAIL_BODY = "Book %s was never returned to the library, contact %s for additional information:\nEmail: %s\nPhone number: %s";
    public static final String BOOK_DELAYED_EMAIL_SUBJECT = "Book return delayed";

    private String recipient;
    private String msgBody;
    private String subject;

    public EmailDetails(String recipient, String subject, String msgBody) {
        this.msgBody = msgBody;
        this.subject = subject;
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
