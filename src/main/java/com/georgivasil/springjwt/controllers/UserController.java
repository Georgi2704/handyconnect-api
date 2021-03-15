package com.georgivasil.springjwt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.repository.RoleRepository;
import com.georgivasil.springjwt.repository.UserRepository;
import com.georgivasil.springjwt.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder encoder;


    @GetMapping("/all")
    public List<User> getUsers() {
        {
            System.out.println("get Users called");

            return userRepo.findAll();
        }
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Optional<User> findUser(@PathVariable long id) {
        Optional<User> userOptional = userRepo.findById(id);

        if (!userOptional.isPresent()) {
            throw new NotFoundException("id-" + id);
        }

        return userRepo.findById(id);
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable long id) {
        Optional<User> userOptional = userRepo.findById(id);

        if (!userOptional.isPresent()) {
            throw new NotFoundException("id-" + id);
        } else {
            User newUser = userOptional.get();
            newUser.setEmail(user.getEmail());
            newUser.setUsername(user.getUsername());
            userRepo.save(newUser);
            return newUser;
        }
    }

//    @CrossOrigin
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    @PutMapping("/user/test/{id}")
//    public ResponseEntity<?> updateUserForUsers(Authentication authentication, @RequestBody UserEdited userEdited, @PathVariable long id) throws JsonProcessingException {
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        long matchID = userDetails.getId();
//        String password = userDetails.getPassword();
//
//        if (matchID == id) {
//            AlreadyExistsError AlreadyExistsError = new AlreadyExistsError();
//            if (userRepo.existsByUsername(userEdited.getNewUsername())) {
//                AlreadyExistsError.setUserAlreadyExists(true);
//            }
//            if (userRepo.existsByEmail(userEdited.getNewEmail())) {
//                AlreadyExistsError.setEmailAlreadyExists(true);
//            }
//            if (AlreadyExistsError.hasAnyErrors()){
//                return ResponseEntity
//                        .badRequest()
//                        .body(AlreadyExistsError);
//            }
//
//            if (encoder.matches(userEdited.getCurrentPassword(), password)) {
//                Optional<User> userOptional = userRepo.findById(id);
//                User newUser = userOptional.get();
//                if (userEdited.getNewEmail() != null && !userEdited.getNewEmail().isEmpty())
//                {
//                    newUser.setEmail(userEdited.getNewEmail());
//                }
//                if (userEdited.getNewPassword() != null && !userEdited.getNewPassword().isEmpty()) {
//                    newUser.setPassword(encoder.encode(userEdited.getNewPassword()));
//                }
//                if (userEdited.getNewUsername() != null && !userEdited.getNewUsername().isEmpty()) {
//                    newUser.setUsername(userEdited.getNewUsername());
//                }
//                userRepo.save(newUser);
//                return ResponseEntity.status(HttpStatus.OK).body("ok");
//            }
//            else {
//                return ResponseEntity
//                        .status(HttpStatus.UNAUTHORIZED)
//                        .body(new MessageResponse("You have entered wrong current password!"));
//            }
//        }
//        else {
//            return ResponseEntity
//                    .status(HttpStatus.FORBIDDEN)
//                    .body("You can't edit other users !");
//        }
//    }

//    @Transactional
//    @CrossOrigin
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    @DeleteMapping("/user/{id}")
//    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
//        Optional<User> user = userRepo.findById(id);
//        if (!user.isPresent()) {
//            throw new UserNotFoundException("id-" + id);
//        }
//        followersRepo.deleteAllByFollowed_Id(id);
//        followersRepo.deleteAllByFollowedBy_Id(id);
//        videoRepo.deleteAllByUploadedBy_Id(id);
//        userRepo.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
