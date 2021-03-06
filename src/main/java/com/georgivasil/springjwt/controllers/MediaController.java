package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.exceptions.NotFoundException;
import com.georgivasil.springjwt.models.Media;
import com.georgivasil.springjwt.repository.MediaRepository;
import com.georgivasil.springjwt.repository.UserRepository;
import com.georgivasil.springjwt.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MediaRepository mediaRepo;

    @CrossOrigin
    @GetMapping("/picture/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getPicture(@PathVariable String filename) {
        Resource file = storageService.load(filename, "picture");
        System.out.println("opa");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @CrossOrigin
    @GetMapping("/picture/id/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getPictureById(@PathVariable Long id) {
        Optional<Media> media = mediaRepo.findById(id);
        if (!media.isPresent()){
            throw new NotFoundException("media not found id: " + id);
        }
        String filename = media.get().getFilename();

        Resource file = storageService.load(filename, "picture");
        System.out.println("opa");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @CrossOrigin
    @GetMapping(value = "/video/{filename:.+}", produces = {"video/mp4"})
    //@ResponseBody
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        Resource file = storageService.load(filename, "video");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
