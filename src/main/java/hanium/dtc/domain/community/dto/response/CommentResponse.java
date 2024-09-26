package hanium.dtc.domain.community.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CommentResponse(

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("content")
        String content,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("commentTime")
        LocalDateTime commentTime,

        @JsonProperty("isReply")
        Boolean isReply,

        @JsonProperty("comment_id")
        Long commentId

) {
}
