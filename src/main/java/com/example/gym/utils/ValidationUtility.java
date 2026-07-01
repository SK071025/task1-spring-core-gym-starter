package com.example.gym.utils;

import com.example.gym.entities.UserEntity;

public class ValidationUtility
{
    private ValidationUtility() {}

    public static void validateUser(UserEntity user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
    }
}
