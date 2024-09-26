package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelRecordListResponse (
    @JsonProperty("records")
    List<TravelRecordResponse> travelRecordResponses
) {
}
