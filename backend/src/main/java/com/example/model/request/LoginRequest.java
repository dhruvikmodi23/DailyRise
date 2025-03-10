package com.example.model.request;
/**
 * Represents a login request containing email address and password.<br>
 * This class is used just for login request handling.
 *
 * @version 1.0.0
 */
public class LoginRequest {
    private String emailAddress;
    private String password;


    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }
}
