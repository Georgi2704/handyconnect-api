package com.georgivasil.springjwt.repository;

import com.georgivasil.springjwt.models.EStatus;
import com.georgivasil.springjwt.models.Offer;
import com.georgivasil.springjwt.models.Problem;
import com.georgivasil.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByHandyman(User handyman);

    List<Offer> findAllByProblem(Problem problem);
}