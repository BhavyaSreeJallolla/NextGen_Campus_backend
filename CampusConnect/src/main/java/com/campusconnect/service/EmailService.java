package com.campusconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("CampusConnect Password Reset");

        message.setText(
                "Hello,\n\n"
                        + "Click the link below to reset your password:\n\n"
                        + resetLink
                        + "\n\nThis link expires in 30 minutes.");

        mailSender.send(message);
    }
}