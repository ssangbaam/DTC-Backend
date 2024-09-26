package hanium.dtc.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HotResponse(

        @JsonProperty("post_id")
        Long postId,

        @JsonProperty("title")
        String title,

        @JsonProperty("content")
        String content,

        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("user")
        UserCommentResponse user,

        @JsonProperty("like")
        Integer like,

        @JsonProperty("place")
        String place

) {
}
