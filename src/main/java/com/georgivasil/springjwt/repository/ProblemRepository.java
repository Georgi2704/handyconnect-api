package com.georgivasil.springjwt.repository;

import com.georgivasil.springjwt.models.EStatus;
import com.georgivasil.springjwt.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByTitle(String title);

    List <Problem> findTop10ByStatusOrderByIdDesc(EStatus status);

    List<Problem> findAllByIdBetweenAndStatusOrderByIdDesc(Long id, Long id2, EStatus status);

//    Optional<Problem> findByProblemContent(String problemContent);
//
//    void deleteByProblemContent(String problemContent);

//    void deleteAllByUploadedBy_Id(Long id);

}