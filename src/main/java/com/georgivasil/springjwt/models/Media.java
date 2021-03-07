package com.georgivasil.springjwt.models;


import javax.persistence.*;
import javax.swing.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "medias")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String filename;

    @NotBlank
    private Boolean isVideo;

    public Media(){

    }

    public Media(String filename, Boolean isVideo){
        this.filename = filename;
        this.isVideo = isVideo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", isVideo=" + isVideo +
                '}';
    }
}
