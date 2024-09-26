package hanium.dtc.domain.community.repository;

import hanium.dtc.domain.community.domain.Comment;
import hanium.dtc.domain.community.domain.CommentLike;
import hanium.dtc.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    void existsByUserAndComment(User user, Comment comment);
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
