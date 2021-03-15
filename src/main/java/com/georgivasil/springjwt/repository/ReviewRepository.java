package com.georgivasil.springjwt.repository;

import com.georgivasil.springjwt.models.EStatus;
import com.georgivasil.springjwt.models.Problem;
import com.georgivasil.springjwt.models.Review;
import com.georgivasil.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByHandyman(User handyman);

}