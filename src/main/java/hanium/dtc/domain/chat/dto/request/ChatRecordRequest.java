package hanium.dtc.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChatRecordRequest(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("other_id")
        Long otherId
) {
}
