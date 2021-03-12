package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.Category;
import com.georgivasil.springjwt.models.Offer;
import com.georgivasil.springjwt.models.Problem;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.payload.response.MessageResponse;
import com.georgivasil.springjwt.repository.*;
import com.georgivasil.springjwt.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    OfferRepository offerRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    ProblemRepository problemRepo;

    @Autowired
    PasswordEncoder encoder;

    @CrossOrigin
    @PreAuthorize("hasRole('HANDYMAN') or hasRole('ADMIN')")
    @PostMapping("/make/{id}")
    public ResponseEntity<MessageResponse> makeOffer(Authentication authentication, @RequestBody Offer offer, @PathVariable long id){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> handyman = userRepo.findById(userID);
        if (!handyman.isPresent()){
            throw new NotFoundException("Handyman not found - id:" + userID);
        }

        Optional<Problem> problem = problemRepo.findById(id);
        if (!problem.isPresent()){
            throw new NotFoundException("Problem not found - id:" + id);
        }
        Offer newOffer = new Offer(offer.getPresumedCost(), offer.getEstimatedTime());
        newOffer.setHandyman(handyman.get());
        newOffer.setProblem(problem.get());

        offerRepo.save(newOffer);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Offer made successfully !"));
    }

    @CrossOrigin
    @PreAuthorize("hasRole('HANDYMAN') or hasRole('ADMIN')")
    @GetMapping(value = "/handyman")
    public List<Offer> getActiveProblemsByCustomer(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> handyman = userRepo.findById(userID);
        return offerRepo.findAllByHandyman(handyman.get());
    }


}
