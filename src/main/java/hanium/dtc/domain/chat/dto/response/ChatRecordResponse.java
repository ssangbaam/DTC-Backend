package hanium.dtc.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRecordResponse (

        @JsonProperty("user_id")
        Long senderId,

        @JsonProperty("recent_chat")
        String recentChat,

        @JsonProperty("user")
        UserCommentResponse userCommentResponse,

        @JsonProperty("recent_time")
        LocalDateTime recentTime
){
}
