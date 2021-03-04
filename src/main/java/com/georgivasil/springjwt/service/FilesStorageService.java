package com.georgivasil.springjwt.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void init();

    public void init_v();

    public void init_t();

    //public void save(MultipartFile file);

    public void save(MultipartFile file, String newFileName, String filetype);

    public Resource load(String filename, String filetype);

    public void deleteAll();

    public void delete(String filename, String filetype) throws IOException;

    public Stream<Path> loadAll(String filetype);


}