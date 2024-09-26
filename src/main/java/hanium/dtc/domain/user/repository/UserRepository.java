package hanium.dtc.domain.user.repository;

import hanium.dtc.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySerialId(Long serialId);
}
