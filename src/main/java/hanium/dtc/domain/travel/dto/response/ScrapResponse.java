package hanium.dtc.domain.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ScrapResponse(
        @JsonProperty("isScrapped") boolean isScrapped,
        @JsonProperty("scrap") int scrap
) {}
