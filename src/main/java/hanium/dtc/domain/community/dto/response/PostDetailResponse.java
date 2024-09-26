package hanium.dtc.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hanium.dtc.domain.travel.dto.response.TravelRecordResponse;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostDetailResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("travel")
        TravelRecordResponse travel,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("comment")
        Integer comment,

        @JsonProperty("scrap")
        Integer scrap,

        @JsonProperty("postTime")
        LocalDateTime postTime,

        @JsonProperty("comments")
        List<CommentResponse> comments
) {
}


