package hanium.dtc.domain.user.dto.Request;

import jakarta.persistence.Column;


public record MyPageUpdateRequest (

        String name,
        String nickname,
        String address,
        Integer age,
        Boolean gender,
        Integer mbti,
        Integer image
){
}
