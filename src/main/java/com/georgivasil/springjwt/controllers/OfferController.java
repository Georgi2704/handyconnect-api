package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.*;
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
    @PostMapping("/make/{problemid}")
    public ResponseEntity<MessageResponse> makeOffer(Authentication authentication, @RequestBody Offer offer, @PathVariable long problemid){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> handyman = userRepo.findById(userID);
        if (!handyman.isPresent()){
            throw new NotFoundException("Handyman not found - id:" + userID);
        }

        Optional<Problem> problem = problemRepo.findById(problemid);
        if (!problem.isPresent()){
            throw new NotFoundException("Problem not found - id:" + problemid);
        }
        Offer newOffer = new Offer(offer.getPresumedCost(), offer.getEstimatedTime(), offer.getAdditionalInfo());
        newOffer.setHandyman(handyman.get());
        newOffer.setProblem(problem.get());

        offerRepo.save(newOffer);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Offer made successfully !"));
    }

    @CrossOrigin
    @PreAuthorize("hasRole('HANDYMAN') or hasRole('ADMIN')")
    @GetMapping(value = "/handyman")
    public List<Offer> getOffersByHandyman(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> handyman = userRepo.findById(userID);
        return offerRepo.findAllByHandyman(handyman.get());
    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping(value = "/problem/{problemid}")
    public List<Offer> getOffersByProblem(@PathVariable long problemid){
        Optional<Problem> problemOpt = problemRepo.findById(problemid);
        if (!problemOpt.isPresent()){
            throw new NotFoundException("Problem not found - id:" + problemid);
        }
        return offerRepo.findAllByProblem(problemOpt.get());
    }
//
//    @CrossOrigin
//    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
//    @GetMapping(value = "/delete/{offerid}")
//    public List<Offer> deleteOffer(@PathVariable long offerId){
//        Optional<Problem> problemOpt = problemRepo.findById(problemid);
//        if (!problemOpt.isPresent()){
//            throw new NotFoundException("Problem not found - id:" + problemid);
//        }
//        return offerRepo.findAllByProblem(problemOpt.get());
//    }

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> changeStatus(@PathVariable long id, @RequestParam("accept") Boolean accept){
        Optional<Offer> offerOpt = offerRepo.findById(id);
        if (!offerOpt.isPresent()){
            throw new NotFoundException("Offer not found - id:" + id);
        }
        Offer offer = offerOpt.get();

        if (accept){
            Optional<Problem> problemOpt = problemRepo.findById(offer.getProblem().getId());
            if (!problemOpt.isPresent()){
                throw new NotFoundException("Problem not found - id:" + id);
            }
            List<Offer> offerList = offerRepo.findAllByProblem(problemOpt.get());

            for (Offer o : offerList) {
                if (!o.getId().equals(offer.getId())){
                    o.setStatus(EStatus.STATUS_REFUSED);
                }
                else {
                    o.setStatus(EStatus.STATUS_ACCEPTED);
                }
                offerRepo.save(o);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Offer accepted successfully !"));
        }
        else if (!accept){
            offer.setStatus(EStatus.STATUS_REFUSED);
            offerRepo.save(offer);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Offer refused successfully !"));

        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new MessageResponse("ddz"));
    }




}
