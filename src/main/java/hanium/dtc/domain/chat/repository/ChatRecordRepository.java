package hanium.dtc.domain.chat.repository;

import hanium.dtc.domain.chat.domain.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
}