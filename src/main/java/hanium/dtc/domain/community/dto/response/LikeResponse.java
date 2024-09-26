package hanium.dtc.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LikeResponse(
        @JsonProperty("isLiked")
        boolean isLiked,

        @JsonProperty("like")
        int like

) {}