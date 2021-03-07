package com.georgivasil.springjwt.repository;

import com.georgivasil.springjwt.models.Media;
import com.georgivasil.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	Optional<Media> findByFilename(String filename);

}
