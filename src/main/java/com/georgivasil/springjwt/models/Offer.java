package com.georgivasil.springjwt.models;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(	name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Valid
    private Problem problem;

    @ManyToOne
    @Valid
    private User handyman;

    private Double presumedCost;

    private Long estimatedTime;

    private EStatus status;

    public Offer(){}

    public Offer(Double presumedCost, Long estimatedTime){
        this.presumedCost = presumedCost;
        this.estimatedTime = estimatedTime;
        this.status = EStatus.STATUS_UNDEFINED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public User getHandyman() {
        return handyman;
    }

    public void setHandyman(User handyman) {
        this.handyman = handyman;
    }

    public Double getPresumedCost() {
        return presumedCost;
    }

    public void setPresumedCost(Double presumedCost) {
        this.presumedCost = presumedCost;
    }

    public Long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", problem=" + problem +
                ", handyman=" + handyman +
                ", presumedCost=" + presumedCost +
                ", estimatedTime=" + estimatedTime +
                ", status=" + status +
                '}';
    }
}
