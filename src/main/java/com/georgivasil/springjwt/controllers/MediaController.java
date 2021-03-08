package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.repository.UserRepository;
import com.georgivasil.springjwt.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

    @CrossOrigin
    @GetMapping("/picture/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getPicture(@PathVariable String filename) {
        Resource file = storageService.load(filename, "picture");
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
