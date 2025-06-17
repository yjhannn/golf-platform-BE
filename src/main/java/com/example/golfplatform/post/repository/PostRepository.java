package com.example.golfplatform.post.repository;

import com.example.golfplatform.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
