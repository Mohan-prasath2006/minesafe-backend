package com.minesafe.minesafe_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =====================================================
    // EMAIL VERIFICATION OTP
    // =====================================================

    public void sendVerificationOtp(
            String email,
            String name,
            String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("MineSafe AR - Email Verification");

        message.setText(
                "Hello " + name + ",\n\n" +
                        "Welcome to MineSafe AR!\n\n" +
                        "Your email verification OTP is:\n\n" +
                        otp + "\n\n" +
                        "Please enter this OTP in the MineSafe AR application " +
                        "to verify your email address.\n\n" +
                        "If you did not create this account, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "MineSafe AR Team"
        );

        mailSender.send(message);
    }

    // =====================================================
    // LOGIN EMAIL
    // =====================================================

    public void sendLoginEmail(
            String email,
            String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("MineSafe AR - Login Successful");

        message.setText(
                "Hello " + name + ",\n\n" +
                        "You have successfully logged in to MineSafe AR.\n\n" +
                        "Your safety training dashboard is ready.\n\n" +
                        "Train Safe. Work Safe. Go Home Safe.\n\n" +
                        "Regards,\n" +
                        "MineSafe AR Team"
        );

        mailSender.send(message);
    }

    // =====================================================
    // PASSWORD RESET OTP
    // =====================================================

    public void sendPasswordResetOtp(
            String email,
            String name,
            String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("MineSafe AR - Password Reset OTP");

        message.setText(
                "Hello " + name + ",\n\n" +
                        "We received a request to reset your MineSafe AR password.\n\n" +
                        "Your password reset OTP is:\n\n" +
                        otp + "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "If you did not request a password reset, " +
                        "please ignore this email.\n\n" +
                        "Regards,\n" +
                        "MineSafe AR Team"
        );

        mailSender.send(message);
    }
}