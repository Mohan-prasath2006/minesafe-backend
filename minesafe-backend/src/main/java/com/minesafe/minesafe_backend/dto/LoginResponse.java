package com.minesafe.minesafe_backend.dto;

public class LoginResponse {

    private String token;
    private String message;
    private String name;
    private String email;

    public LoginResponse(
            String token,
            String message,
            String name,
            String email) {

        this.token = token;
        this.message = message;
        this.name = name;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}