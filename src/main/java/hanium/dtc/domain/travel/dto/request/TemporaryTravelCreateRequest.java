package hanium.dtc.domain.travel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TemporaryTravelCreateRequest(
        @JsonProperty("depart_at")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate departAt,

        @JsonProperty("arrive_at")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate arriveAt
) {
}
