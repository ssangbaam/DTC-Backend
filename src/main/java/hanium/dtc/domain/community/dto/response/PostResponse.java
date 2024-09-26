package hanium.dtc.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.Builder;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public record PostResponse(

        @JsonProperty("post_id")
        Long postId,

        @JsonProperty("title")
        String title,

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment")
        Integer comment,

        @JsonProperty("scrap")
        Integer scrap,

        @JsonProperty("post_time")
        LocalDateTime postTime

) {
}

