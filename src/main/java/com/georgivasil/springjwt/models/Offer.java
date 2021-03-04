package com.georgivasil.springjwt.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotBlank
    @Valid
    private Problem problem;

    @ManyToOne
    @NotBlank
    @Valid
    private User handyman;

    @NotBlank
    private Double presumedCost;

    @NotBlank
    private Long estimatedTime;

    @NotBlank
    private EStatus status;

}
