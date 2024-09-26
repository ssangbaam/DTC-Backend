package hanium.dtc.domain.community.repository;

import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.community.domain.PostLike;
import hanium.dtc.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void existsByUserAndPost(User user, Post post);
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
