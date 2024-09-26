package hanium.dtc.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.chat.domain.ChatType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatDetailResponse(

        @JsonProperty("user_id")
        Long senderId,

        String content,

        @JsonProperty("chat_time")
        LocalDateTime chatTime,

        @JsonProperty("type")
        ChatType chatType
) {
}
