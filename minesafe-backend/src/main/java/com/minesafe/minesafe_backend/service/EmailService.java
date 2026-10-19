package com.minesafe.minesafe_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final RestClient restClient;

    @Value("${resend.api.key}")
    private String resendApiKey;

    public EmailService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.resend.com")
                .build();
    }

    private void sendEmail(
            String email,
            String subject,
            String text) {

        Map<String, Object> body = Map.of(
                "from", "MineSafe AR <onboarding@resend.dev>",
                "to", List.of(email),
                "subject", subject,
                "text", text
        );

        restClient.post()
                .uri("/emails")
                .header("Authorization", "Bearer " + resendApiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    // EMAIL VERIFICATION OTP
    public void sendVerificationOtp(
            String email,
            String name,
            String otp) {

        String text =
                "Hello " + name + ",\n\n" +
                "Welcome to MineSafe AR!\n\n" +
                "Your email verification OTP is:\n\n" +
                otp + "\n\n" +
                "Enter this OTP in the MineSafe AR application " +
                "to verify your email address.\n\n" +
                "If you did not create this account, please ignore this email.\n\n" +
                "Regards,\nMineSafe AR Team";

        sendEmail(
                email,
                "MineSafe AR - Email Verification",
                text
        );
    }

    // LOGIN EMAIL
    public void sendLoginEmail(
            String email,
            String name) {

        String text =
                "Hello " + name + ",\n\n" +
                "You have successfully logged in to MineSafe AR.\n\n" +
                "Your safety training dashboard is ready.\n\n" +
                "Train Safe. Work Safe. Go Home Safe.\n\n" +
                "Regards,\nMineSafe AR Team";

        sendEmail(
                email,
                "MineSafe AR - Login Successful",
                text
        );
    }

    // PASSWORD RESET OTP
    public void sendPasswordResetOtp(
            String email,
            String name,
            String otp) {

        String text =
                "Hello " + name + ",\n\n" +
                "We received a request to reset your MineSafe AR password.\n\n" +
                "Your password reset OTP is:\n\n" +
                otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you did not request a password reset, " +
                "please ignore this email.\n\n" +
                "Regards,\nMineSafe AR Team";

        sendEmail(
                email,
                "MineSafe AR - Password Reset OTP",
                text
        );
    }
}