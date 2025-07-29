package com.example.golfplatform.review.repository;

import com.example.golfplatform.review.domain.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    default List<Review> findRecentReview(int size) {
        Page<Review> page =
            findAll(PageRequest.of(0, size, org.springframework.data.domain.Sort.by("createdAt").descending()));
        return page.getContent();
    }
}
