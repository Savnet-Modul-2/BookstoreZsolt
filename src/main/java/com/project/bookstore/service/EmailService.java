package com.project.bookstore.service;

import com.project.bookstore.helper.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(EmailDetails details) {
        System.out.println(Thread.currentThread().getName());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());
        mailSender.send(mailMessage);
    }
}
