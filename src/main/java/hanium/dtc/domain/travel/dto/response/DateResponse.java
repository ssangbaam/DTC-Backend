package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DateResponse(
        @JsonProperty("date")
        Integer date,

        @JsonProperty("day")
        String day,

        @JsonProperty("dday")
        String dday,

        @JsonProperty("day_int")
        Integer dayInt
) {
}
