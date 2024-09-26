package hanium.dtc.domain.travel.repository;

import hanium.dtc.domain.travel.domain.TemporaryRecommend;
import hanium.dtc.domain.travel.domain.TemporaryTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemporaryRecommendRepository extends JpaRepository<TemporaryRecommend, Long> {
    @Query("SELECT recommend FROM TemporaryRecommend WHERE temporaryTravel = :travelId")
    List<String> findAllRecommendByTemporaryTravel(@Param("travelId") TemporaryTravel temporaryTravel);

    @Query("SELECT t FROM TemporaryRecommend t WHERE t.day = :day AND t.temporaryTravel = :temporaryTravel")
    List<TemporaryRecommend> findByDayAndTemporaryTravel(@Param("day") String day, @Param("temporaryTravel") TemporaryTravel temporaryTravel);
}
