package com.georgivasil.springjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/problem")
public class ProblemController {

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
    PasswordEncoder encoder;

    @Autowired
    FilesStorageService storageService;

    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('HANDYMAN') or hasRole('ADMIN')")
    @PostMapping("/post")
    public ResponseEntity<MessageResponse> postProblem(Authentication authentication, @RequestParam("problem") String videoString, @RequestParam("file") MultipartFile file) throws IOException {
        Problem problem  = new ObjectMapper().readValue(videoString, Problem.class);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> customer = userRepo.findById(userID);
        System.out.println(customer.toString());

        List<Category> categories = new ArrayList<>();
        for (Category c : problem.getCategories()) {
            Optional<Category> categoryOptional = categoryRepo.findByTitle(c.getTitle());
            if (!categoryOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Category not found: " + c.getTitle()));
            }
            else{
                categories.add(categoryOptional.get());
            }
        }

        for (Category c:categories) {
            System.out.println("Category:" + c.toString());
        }

        String message = "";
//        try {
            String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String newFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), FilenameUtils.getBaseName(file.getOriginalFilename()).concat(currentDate) + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toLowerCase();
            System.out.println(file.getSize());
            final long limit = 100 * 1024 * 1024;
            if (file.getSize() > limit) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Your picture file is too large."));
            }
            storageService.save(file, newFileName, "picture");

            Media picture = new Media(newFileName, false);
            System.out.println("Picture :" + picture);
            List<Media> medias = new ArrayList<>();
            medias.add(picture);
            for (Media m:medias) {
                System.out.println("Media" + m.toString());
            }

                Problem newProblem = new Problem(
                        customer.get(),
                        problem.getTitle(),
                        problem.getDescription(),
                        problem.getLocation(),
                        categories,
                        medias
                );

            System.out.println("Problem:" + newProblem.toString());
            problemRepo.save(newProblem);
            message = newFileName;
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public Problem getProblem(@PathVariable Long id){
        Optional<Problem> problem = problemRepo.findById(id);


        if (problem.isPresent()){
            Problem videoReal = problem.get();
            Problem updateViews = problem.get();
            User u = videoReal.getCustomer();
            u.setPassword("");
            videoReal.setCustomer(u);
            return videoReal;
        }
        else {
            throw new NotFoundException("problem not found" + id);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/from/{id}")
    public List<Problem> get10Problems_Unfixed(@PathVariable Long id){
        List<Problem> problems = new ArrayList<>();
        if (id == 0) {
            problems = problemRepo.findTop10ByStatusOrderByIdDesc(EStatus.STATUS_UNDEFINED);
        }
        else {
            problems = problemRepo.findAllByIdBetweenAndStatusOrderByIdDesc(id-20, id-1, EStatus.STATUS_UNDEFINED);
        }
        return problems;
    }


    @CrossOrigin
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('HANDYMAN') or hasRole('ADMIN')")
    @GetMapping(value = "/customer")
    public List<Problem> getActiveProblemsByCustomer(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        long userID = userDetails.getId();
        Optional<User> customer = userRepo.findById(userID);
        return problemRepo.findAllByCustomer(customer.get());
    }
}
