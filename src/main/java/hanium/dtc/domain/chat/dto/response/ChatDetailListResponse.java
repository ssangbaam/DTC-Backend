package hanium.dtc.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatDetailListResponse(
        @JsonProperty("details")
        List<ChatDetailResponse> chatDetailResponses
) {
}
