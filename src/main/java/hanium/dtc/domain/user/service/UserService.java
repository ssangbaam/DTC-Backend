package hanium.dtc.domain.user.service;

import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.dto.Request.MyPageUpdateRequest;
import hanium.dtc.domain.user.dto.Response.MyPageResponse;
import hanium.dtc.domain.user.dto.Response.MyPageUpdateResponse;
import hanium.dtc.domain.user.repository.UserRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_USER));
        MyPageResponse myPageResponse = MyPageResponse.builder()
                .age(user.getAge())
                .image(user.getImage())
                .mbti(user.getMbti())
                .gender(user.getGender())
                .nickname(user.getNickname())
                .build();

        return myPageResponse;
    }

    @Transactional(readOnly = true)
    public MyPageUpdateResponse getMyPageUpdate(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_USER));
        MyPageUpdateResponse myPageUpdateResponse = MyPageUpdateResponse.builder()
                .image(user.getImage())
                .name(user.getName())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .mbti(user.getMbti())
                .build();

        return myPageUpdateResponse;
    }

    @Transactional
    public void updateMyPage(MyPageUpdateRequest request, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_USER));

        user.update(request);
    }
}
