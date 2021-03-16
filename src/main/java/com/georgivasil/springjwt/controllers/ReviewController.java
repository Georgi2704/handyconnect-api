package com.georgivasil.springjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.Media;
import com.georgivasil.springjwt.models.Problem;
import com.georgivasil.springjwt.models.Review;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.payload.response.MessageResponse;
import com.georgivasil.springjwt.repository.*;
import com.georgivasil.springjwt.security.services.UserDetailsImpl;
import com.georgivasil.springjwt.service.FilesStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public ResponseEntity<?> postReview(@PathVariable Long problemid, @PathVariable Long handymanid, @RequestParam("review") String review,  @RequestParam("file") MultipartFile file) throws IOException {
        Review review01  = new ObjectMapper().readValue(review, Review.class);
        Optional<Problem> problemOpt = problemRepo.findById(problemid);
        if (!problemOpt.isPresent()){
            throw new NotFoundException("Problem not found - id:" + problemid);
        }
        Problem problem = problemOpt.get();

        Optional<User> handyman = userRepo.findById(handymanid);
        if (!handyman.isPresent()){
            throw new NotFoundException("Handyman not found - id:" + handymanid);
        }
        String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String newFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), FilenameUtils.getBaseName(file.getOriginalFilename()).concat(currentDate) + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toLowerCase();
        System.out.println(file.getSize());
        final long limit = 100 * 1024 * 1024;
        if (file.getSize() > limit) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Your picture file is too large."));
        }
        storageService.save(file, newFileName, "picture");
        Media picture = new Media(newFileName, false);
        mediaRepo.save(picture);

        problem.setFinalResult(picture);

        Review review1 = new Review(review01.getDescription(), review01.getFixedSuccessfully(), review01.getRating());

        review1.setHandyman(handyman.get());
        review1.setProblem(problem);

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
