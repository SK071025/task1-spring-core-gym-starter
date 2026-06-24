package com.example.gym.services;

import java.security.SecureRandom;

public class CredentialsService
{
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private final SecureRandom random = new SecureRandom();

    public String generateUsername(String firstName, String lastName, boolean exists, int serialNumber) {
        String baseUsername = firstName + "." + lastName;
        return exists ? baseUsername + serialNumber : baseUsername;
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}
