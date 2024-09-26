package hanium.dtc.domain.travel.repository;

import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.travel.domain.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {
    Optional<TravelRecord> findByUserAndId(User user, Long travelId);
    List<TravelRecord> findByUser(User user);
}
