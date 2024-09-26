package hanium.dtc.domain.community.repository;

import java.util.Optional;
import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.travel.domain.TravelRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByIsTravel(boolean isTravel, Pageable pageable);
    Page<Post> findByPostLikeGreaterThanEqual(int likeThreshold, Pageable pageable);
    Optional<Post> findByTravelRecord(TravelRecord travelRecord);
}