package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TravelDetailResponse (
        @JsonProperty("title")
        String title,

        @JsonProperty("depart_at")
        LocalDate departAt,

        @JsonProperty("arrive_at")
        LocalDate arriveAt
) {
}

