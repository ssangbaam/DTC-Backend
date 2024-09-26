package hanium.dtc.auth.service;

import hanium.dtc.auth.dto.request.SignUpRequest;
import hanium.dtc.auth.dto.response.AuthorizationResponse;
import hanium.dtc.auth.dto.response.KakaoUserInfoResponse;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.repository.UserRepository;
import hanium.dtc.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthorizationResponse login(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoService.getUserInfo(accessToken);
        Long serialId = kakaoUserInfoResponse.getId();

        User user = userRepository.findBySerialId(serialId).orElseGet(
                () -> userRepository.save(new User(serialId))
        );
        return AuthorizationResponse.of(user.getNickname() != null, jwtUtil.generateTokens(user.getId()));
    }

    @Transactional
    public Boolean signUp(Long userId, SignUpRequest signUpRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        user.signUpUser(signUpRequest);

        return Boolean.TRUE;
    }

}
