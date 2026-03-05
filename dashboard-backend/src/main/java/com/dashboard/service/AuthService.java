package com.dashboard.service;

import org.jvnet.hk2.annotations.Service;

import com.dashboard.model.User;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Service
public class AuthService {
    
    private static final User STATIC_USER = new User("admin", "password123");
    
    public boolean authenticate(String username, String password) {
        return STATIC_USER.getUsername().equals(username) && 
               STATIC_USER.getPassword().equals(password);
    }
    
    public User getUser() {
        return User.builder()
                .username(STATIC_USER.getUsername())
                .password("********")  // Ne pas renvoyer le vrai mot de passe
                .build();
    }
}