package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.repository.*;
import com.georgivasil.springjwt.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    MediaRepository mediaRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    ProblemRepository problemRepo;
    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    FilesStorageService storageService;

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/post/{problemid}")
    public ResponseEntity<?> postReview(Authentication authentication, @PathVariable Long problemid) throws IOException {
        return null;
    }
}
