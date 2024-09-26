package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record RecordDetailResponse (
        @JsonProperty("id")
        Long id,

        @JsonProperty("title")
        String title,

        @JsonProperty("thema")
        String thema,

        @JsonProperty("detail_address")
        String detailAddress,

        @JsonProperty("plan")
        String plan,

        @JsonProperty("start_at")
        LocalDateTime startAt,

        @JsonProperty("end_at")
        LocalDateTime endAt,

        @JsonProperty("lat")
        Double lat,

        @JsonProperty("lon")
        Double lon
) {
}
