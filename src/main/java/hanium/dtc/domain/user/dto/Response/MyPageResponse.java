package hanium.dtc.domain.user.dto.Response;

import lombok.Builder;

@Builder
public record MyPageResponse (
    String nickname,
    Integer age,
    Boolean gender,
    Integer mbti,
    Integer image
)
{}
