package hanium.dtc.domain.community.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LikeRequest(
        @JsonProperty("postId")
        Long postId,

        @JsonProperty("commentId")
        Long commentId
) {}
