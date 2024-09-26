package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record DetailInfoResponse(
        @JsonProperty("title")
        String title,

        @JsonProperty("thema")
        String thema,

        @JsonProperty("address")
        String address,

        @JsonProperty("lat")
        Double lat,

        @JsonProperty("lon")
        Double lon
) {
}
