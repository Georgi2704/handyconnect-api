package com.georgivasil.springjwt.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

    private final Path root = Paths.get("uploaded_pics");
    private final Path root_v = Paths.get("uploaded_videos");
    private final Path root_t = Paths.get("uploaded_thumbnails");

    @Override
    public void init() {
        try {
            Files.newDirectoryStream(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload pfp!");
        }
    }

    @Override
    public void init_v() {
        try {
            Files.newDirectoryStream(root_v);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload videos!");
        }
    }

    @Override
    public void init_t() {
        try {
            Files.newDirectoryStream(root_t);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload thumbnails!");
        }
    }

//    @Override
//    public void save(MultipartFile file) {
//        try {
//            //init();
//            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
//        } catch (Exception e) {
//            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//        }
//    }

    @Override
    public void save(MultipartFile file, String newFileName, String filetype) { //Add user id here
        try {
            if (filetype == "picture") {
                init();
                Files.copy(file.getInputStream(), this.root.resolve(newFileName));//and in resolve ()
            }
            else if (filetype == "video"){
                init_v();
                Files.copy(file.getInputStream(), this.root_v.resolve(newFileName));//and in resolve ()
            }
            else if (filetype == "thumbnail"){
                init_t();
                Files.copy(file.getInputStream(), this.root_t.resolve(newFileName));//and in resolve ()
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename, String filetype) {
        try {
            Resource resource = null;
            if (filetype == "picture") {
                Path file = root.resolve(filename);
                resource = new UrlResource(file.toUri());
            }
            else if (filetype == "video"){
                Path file = root_v.resolve(filename);
                resource = new UrlResource(file.toUri());
            }
            else if (filetype == "thumbnail"){
                Path file = root_t.resolve(filename);
                resource = new UrlResource(file.toUri());
            }
            else {
                throw new RuntimeException("Wrong filetype specified !");
            }

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    //FileStorageServiceImpl
    @Override
    public void delete(String filename, String filetype) throws IOException {
        try {
            Resource resource = null;
            Path file = null;
            if (filetype == "picture") {
                file = root.resolve(filename);
                resource = new UrlResource(file.toUri());
            }
            else if (filetype == "video"){
                file = root_v.resolve(filename);
                resource = new UrlResource(file.toUri());
            }
            else if (filetype == "thumbnail"){
                file = root_t.resolve(filename);
                resource = new UrlResource(file.toUri());
            }

            if (resource != null)
            {
                if (resource.exists() || resource.isReadable()) {
                    FileSystemUtils.deleteRecursively(file.toFile());
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll(String filetype) {
        try {
            if (filetype == "picture") {
                return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
            }
            else if (filetype == "video"){
                return Files.walk(this.root_v, 1).filter(path -> !path.equals(this.root_v)).map(this.root_v::relativize);
            }
            else if (filetype == "thumbnail"){
                return Files.walk(this.root_t, 1).filter(path -> !path.equals(this.root_t)).map(this.root_t::relativize);
            }
            else {
                throw new RuntimeException("Wrong filetype specified");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}