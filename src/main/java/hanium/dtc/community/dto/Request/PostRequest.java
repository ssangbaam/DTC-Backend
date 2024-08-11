package hanium.dtc.community.dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PostRequest(
        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("travel_id")
        Long travelId
) {
}