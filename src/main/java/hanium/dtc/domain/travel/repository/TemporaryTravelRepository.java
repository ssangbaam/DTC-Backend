package hanium.dtc.domain.travel.repository;

import hanium.dtc.domain.travel.domain.TemporaryTravel;
import hanium.dtc.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryTravelRepository extends JpaRepository<TemporaryTravel, Long> {
    TemporaryTravel findByUser(User user);
}
