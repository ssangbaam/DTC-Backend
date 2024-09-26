package hanium.dtc.domain.chat.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.chat.domain.ChatType;
import lombok.Builder;


@Builder
public record ChatDetailRequest (
        @JsonProperty("content")
        String content,

        @JsonProperty("chat_type")
        ChatType chatType
){
}
