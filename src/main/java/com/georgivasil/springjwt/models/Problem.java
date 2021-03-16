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
@Table(	name = "problems")

public class Problem {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne
        @Valid
        private User customer;

        @NotBlank
        @Size(max = 30)
        private String title;

        @Size(max = 250)
        private String description;

        @NotBlank
        @Size(max = 100)
        private String location;

        @ManyToMany
        @JoinTable(	name = "problems_categories",
                joinColumns = @JoinColumn(name = "problem_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
        private List<Category> categories = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
        @JoinTable(	name = "problems_medias",
                joinColumns = @JoinColumn(name = "problem_id"),
                inverseJoinColumns = @JoinColumn(name = "media_id"))
        private List<Media> media = new ArrayList<>();

        @ManyToOne
        @Valid
        private Media finalResult;

        private EStatus status;

        private String posted_at;

        private String fixed_at;

        public Problem() {
        }

        public Problem(User customer, String title, String description, String location, List<Category> categories, List<Media> media) {
            this.customer = customer;
            this.title = title;
            this.description = description;
            this.location = location;
            this.categories = categories;
            this.media = media;
            this.status = EStatus.STATUS_UNDEFINED;
            this.posted_at = LocalDateTime.now().toString();
        }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public String getFixed_at() {
        return fixed_at;
    }

    public void setFixed_at(String fixed_at) {
        this.fixed_at = fixed_at;
    }

    public Media getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Media finalResult) {
        this.finalResult = finalResult;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", customer=" + customer +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", categories=" + categories +
                ", media=" + media +
                ", status=" + status +
                ", posted_at='" + posted_at + '\'' +
                ", fixed_at='" + fixed_at + '\'' +
                '}';
    }
}
