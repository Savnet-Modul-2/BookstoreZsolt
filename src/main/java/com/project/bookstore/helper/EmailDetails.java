package com.project.bookstore.helper;

public class EmailDetails {
    public static final String CODE_EMAIL_SUBJECT="Your code for the Bookstore App";
    public static final String CODE_EMAIL_STRING="Your verification code for your account is: %s\n This code is valid for 1 hour.";

    private String recipient;
    private String msgBody;
    private String subject;


    public EmailDetails(String recipient, String subject, String msgBody) {
        this.msgBody = msgBody;
        this.subject = subject;
        this.recipient=recipient;
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
