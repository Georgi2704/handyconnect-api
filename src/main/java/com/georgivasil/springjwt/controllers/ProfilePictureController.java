package com.georgivasil.springjwt.controllers;

import com.georgivasil.springjwt.models.FileInfo;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.payload.response.MessageResponse;
import com.georgivasil.springjwt.repository.UserRepository;
import com.georgivasil.springjwt.service.FilesStorageService;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/file")
public class ProfilePictureController {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

    //    Upload 1 file
    @PostMapping("/upload/profilepic/user/{id}")
    public ResponseEntity<MessageResponse> uploadFile(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) { //Add user id here
        String message = "";

        System.out.println(file.getOriginalFilename());

        try {
            Optional<User> userData = userRepository.findById(id);
            System.out.println(userData);
            if (userData.isPresent()) {
                User currentUser = userData.get();
                String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                String newFileName = file.getOriginalFilename().replace(file.getOriginalFilename(), FilenameUtils.getBaseName(file.getOriginalFilename()).concat(currentDate) + "." + FilenameUtils.getExtension(file.getOriginalFilename())).toLowerCase();
                System.out.println(file.getSize());
                final long limit = 10 * 1024 * 1024;
                if (file.getSize() > limit) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Your file is too large."));
                }
                storageService.save(file, newFileName, "picture");
                currentUser.setProfilePic(newFileName);
                userRepository.save(currentUser);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll("picture").map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ProfilePictureController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/profilepic/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename, "picture");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @DeleteMapping("/profilepic/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String filename) throws IOException {
        //Resource file = storageService.load(filename);
        try{
            storageService.delete(filename, "picture");
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File deleted successfully");
    }
}