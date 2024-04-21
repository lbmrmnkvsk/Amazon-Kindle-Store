package org.example.ebookstore.controllers;

import jakarta.validation.Valid;
import org.example.ebookstore.entities.dtos.UserRegistrationDto;
import org.example.ebookstore.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String displayRegistrationPage() {
        return "user-register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            this.userService.createUser(userRegistrationDto);
            return ResponseEntity.ok("You have registered successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public String displayUserProfileUpdatePage(Model model) {
        return "user-profile";
    }

    @GetMapping("/orders")
    public String displayUserOrdersPage(Model model) {
        return "user-orders";
    }

    @GetMapping("/ratings")
    public String displayUserRatingsPage(Model model) {
        return "user-ratings";
    }

    @GetMapping("/reviews")
    public String displayUserReviewsPage(Model model) {
        return "user-reviews";
    }
}
