package com.ajogious.e_com_backend.utils;

import com.ajogious.e_com_backend.entities.User;
import com.ajogious.e_com_backend.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName(); // get username (usually email) from token
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found in database"));
        }

        throw new RuntimeException("User not authenticated");
    }
}
