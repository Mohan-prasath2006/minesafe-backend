package com.minesafe.minesafe_backend.controller;

import com.minesafe.minesafe_backend.dto.LoginRequest;
import com.minesafe.minesafe_backend.dto.LoginResponse;
import com.minesafe.minesafe_backend.entity.User;
import com.minesafe.minesafe_backend.repository.UserRepository;
import com.minesafe.minesafe_backend.security.JwtService;
import com.minesafe.minesafe_backend.service.EmailService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // REGISTER - NO EMAIL VERIFICATION

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Email and password are required");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setEmailVerified(true);
        user.setVerificationOtp(null);

        userRepository.save(user);

        return ResponseEntity.ok(
                "Account created successfully. You can now login."
        );
    }

    // LOGIN - NO EMAIL VERIFICATION OR LOGIN EMAIL

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail()
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        "Login successful",
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    // PROFILE - JWT PROTECTED

    @GetMapping("/profile")
    public ResponseEntity<?> profile(
            org.springframework.security.core.Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                Map.of(
                        "email", email,
                        "message", "JWT authentication successful"
                )
        );
    }

    // FORGOT PASSWORD - EMAIL OTP RETAINED

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email) {

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("No account found with this email");
        }

        String resetOtp = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        long expiryTime = System.currentTimeMillis()
                + (5 * 60 * 1000);

        user.setResetOtp(resetOtp);
        user.setResetOtpExpiry(expiryTime);

        userRepository.save(user);

        emailService.sendPasswordResetOtp(
                user.getEmail(),
                user.getName(),
                resetOtp
        );

        return ResponseEntity.ok(
                "Password reset OTP sent to your email."
        );
    }

    // VERIFY PASSWORD RESET OTP

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("User not found");
        }

        if (user.getResetOtp() == null) {
            return ResponseEntity.badRequest()
                    .body("No password reset request found");
        }

        if (user.getResetOtpExpiry() == null
                || System.currentTimeMillis()
                > user.getResetOtpExpiry()) {

            user.setResetOtp(null);
            user.setResetOtpExpiry(null);
            userRepository.save(user);

            return ResponseEntity.badRequest()
                    .body("OTP expired. Please request a new OTP.");
        }

        if (!otp.equals(user.getResetOtp())) {
            return ResponseEntity.badRequest()
                    .body("Invalid OTP");
        }

        return ResponseEntity.ok(
                "OTP verified successfully"
        );
    }

    // RESET PASSWORD

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("User not found");
        }

        if (user.getResetOtp() == null) {
            return ResponseEntity.badRequest()
                    .body("No password reset request found");
        }

        if (user.getResetOtpExpiry() == null
                || System.currentTimeMillis()
                > user.getResetOtpExpiry()) {

            user.setResetOtp(null);
            user.setResetOtpExpiry(null);
            userRepository.save(user);

            return ResponseEntity.badRequest()
                    .body("OTP expired. Please request a new OTP.");
        }

        if (!otp.equals(user.getResetOtp())) {
            return ResponseEntity.badRequest()
                    .body("Invalid OTP");
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest()
                    .body("Password must contain at least 6 characters");
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        user.setResetOtp(null);
        user.setResetOtpExpiry(null);

        userRepository.save(user);

        return ResponseEntity.ok(
                "Password reset successfully. You can now login."
        );
    }
}