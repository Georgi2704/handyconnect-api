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
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @NotBlank
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

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(	name = "problems_categories",
                joinColumns = @JoinColumn(name = "problem_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
        private Set<Category> categories = new HashSet<>();

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(	name = "problems_medias",
                joinColumns = @JoinColumn(name = "problem_id"),
                inverseJoinColumns = @JoinColumn(name = "media_id"))
        private List<Media> media = new ArrayList<>();

        @NotBlank
        private EStatus status;

        @NotBlank
        private String posted_at;

        private String fixed_at;

        public Problem() {
        }

        public Problem(User customer, String title, String description, String location, Set<Category> categories, List<Media> media) {
            this.customer = customer;
            this.title = title;
            this.description = description;
            this.location = location;
            this.categories = categories;
            this.media = media;
            this.status = EStatus.STATUS_UNDEFINED;
            this.posted_at = LocalDateTime.now().toString();
        }

    }
