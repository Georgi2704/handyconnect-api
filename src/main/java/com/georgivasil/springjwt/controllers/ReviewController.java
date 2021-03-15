package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.Problem;
import com.georgivasil.springjwt.models.Review;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.payload.response.MessageResponse;
import com.georgivasil.springjwt.repository.*;
import com.georgivasil.springjwt.security.services.UserDetailsImpl;
import com.georgivasil.springjwt.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewRepository reviewRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    MediaRepository mediaRepo;
    @Autowired
    ProblemRepository problemRepo;
    @Autowired
    FilesStorageService storageService;

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/post/{handymanid}/{problemid}")
    public ResponseEntity<?> postReview(@PathVariable Long problemid, @PathVariable Long handymanid, @RequestBody Review review){
        Optional<Problem> problem = problemRepo.findById(problemid);
        if (!problem.isPresent()){
            throw new NotFoundException("Problem not found - id:" + problemid);
        }
        Optional<User> handyman = userRepo.findById(handymanid);
        if (!handyman.isPresent()){
            throw new NotFoundException("Handyman not found - id:" + handymanid);
        }
        Review review1 = new Review(review.getDescription(), review.getFixedSuccessfully(), review.getRating());

        review1.setHandyman(handyman.get());
        review1.setProblem(problem.get());
        reviewRepo.save(review1);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Review made successfully !"));
//        return review1;
    }

    @CrossOrigin
    @GetMapping("/all/{handymanid}")
    public List<Review> getAllReviewsByHandyman(@PathVariable Long handymanid){
        Optional<User> handyman = userRepo.findById(handymanid);
        if (!handyman.isPresent()){
            throw new NotFoundException("Handyman not found - id:" + handymanid);
        }

        List<Review> reviews = reviewRepo.findAllByHandyman(handyman.get());
        return reviews;
    }
}
