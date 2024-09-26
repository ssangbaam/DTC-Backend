package hanium.dtc.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatRecordListResponse(
        @JsonProperty("records")
        List<ChatRecordResponse> chatRecordResponses
) {

}
