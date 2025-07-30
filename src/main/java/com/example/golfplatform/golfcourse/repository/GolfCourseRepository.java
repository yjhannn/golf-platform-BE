package com.example.golfplatform.golfcourse.repository;

import com.example.golfplatform.golfcourse.domain.GolfCourse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GolfCourseRepository extends JpaRepository<GolfCourse, Long> {
    Optional<GolfCourse> findByNameAndAddress(String name, String address);
}
