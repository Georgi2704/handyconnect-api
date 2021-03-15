package com.georgivasil.springjwt.models;

import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "reviews")

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @Valid
    private User handyman;

    @ManyToOne
    @Valid
    private Problem problem;

    @Size(max = 500)
    private String description;

    private Boolean isFixedSuccessfully;

    private String posted_at;

    private Double rating;

    public Review() {
    }

    public Review(User handyman, Problem problem, String description, Boolean isFixedSuccessfully, Double rating) {
        this.handyman = handyman;
        this.problem = problem;
        this.description = description;
        this.isFixedSuccessfully = isFixedSuccessfully;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getHandyman() {
        return handyman;
    }

    public void setHandyman(User handyman) {
        this.handyman = handyman;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFixedSuccessfully() {
        return isFixedSuccessfully;
    }

    public void setFixedSuccessfully(Boolean fixedSuccessfully) {
        isFixedSuccessfully = fixedSuccessfully;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", handyman=" + handyman +
                ", problem=" + problem +
                ", description='" + description + '\'' +
                ", isFixedSuccessfully=" + isFixedSuccessfully +
                ", posted_at='" + posted_at + '\'' +
                ", rating=" + rating +
                '}';
    }
}
