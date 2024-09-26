package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
public record TravelRecordResponse (

    @JsonProperty("title")
    String title,

    @JsonProperty("place")
    String place,

    @JsonProperty("depart_at")
    LocalDate departAt,

    @JsonProperty("arrive_at")
    LocalDate arriveAt,

    @JsonProperty("image_url")
    String imageUrl,

    @JsonProperty("travel_record_id")
    Long travelRecordId

) {
}
