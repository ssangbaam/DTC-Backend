package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelRecordDetailResponse (
        @JsonProperty("dates")
        List<DateResponse> dateResponses,

        @JsonProperty("travel")
        TravelDetailResponse travelDetailResponse,

        @JsonProperty("travel_details")
        List<RecordDetailResponse> recordDetailResponses
) {
}
