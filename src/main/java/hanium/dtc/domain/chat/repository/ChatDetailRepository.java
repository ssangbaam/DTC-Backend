package hanium.dtc.domain.chat.repository;

import hanium.dtc.domain.chat.domain.ChatDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatDetailRepository extends JpaRepository<ChatDetail, Long> {
}
