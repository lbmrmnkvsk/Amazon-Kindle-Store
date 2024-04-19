package org.example.ebookstore.controllers;

import jakarta.validation.Valid;
import org.example.ebookstore.entities.dtos.RatingResultDto;
import org.example.ebookstore.entities.dtos.RatingSubmissionDto;
import org.example.ebookstore.services.interfaces.RatingService;
import org.example.ebookstore.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @PostMapping("/books/{bookId}/rate")
    public ResponseEntity<?> rateBook(@PathVariable("bookId") Long bookId,
                                      @Valid @RequestBody RatingSubmissionDto ratingSubmissionDto,
                                      Model model) {
        try {
            RatingResultDto result = this.ratingService.createRating(ratingSubmissionDto.getRating(),
                    model, bookId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/books/{bookId}/rate")
    public ResponseEntity<?> deleteRating(@PathVariable("bookId") Long bookId, Model model) {
        try {
            String response = this.ratingService.deleteRating(bookId, model);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
