package hanium.dtc.domain.travel.repository;

import hanium.dtc.domain.travel.domain.TravelRecord;
import hanium.dtc.domain.travel.domain.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
    Optional<List<RecordDetail>> findByTravelRecordAndDay(TravelRecord travelRecord, Integer day);
}