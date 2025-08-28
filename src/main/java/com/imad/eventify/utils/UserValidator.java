package com.imad.eventify.utils;

import com.imad.eventify.Exceptions.InactiveUserException;
import com.imad.eventify.model.entities.User;

public class UserValidator {

    public static void assertUserIsActive(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.isActive()) {
            throw new InactiveUserException("Inactive users cannot perform this action");
        }
    }

}
