package hanium.dtc.domain.community.controller;

import hanium.dtc.domain.community.dto.response.LikeResponse;
import hanium.dtc.domain.community.service.LikeService;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.annotation.UserId;
import hanium.dtc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;
    private final UserRepository userRepository;

    @PostMapping("/community/{postId}/like")
    public ResponseDto<LikeResponse> togglePostLike(@UserId Long userId, @PathVariable Long postId) {

        boolean isLiked = likeService.togglePostLike(postId, userId);
        int like = likeService.getPostLike(postId);

        LikeResponse response = LikeResponse.builder()
                .isLiked(isLiked)
                .like(like)
                .build();

        return ResponseDto.ok(response);
    }

    @PostMapping("/comments/{postId}/{commentId}/like")
    public ResponseDto<LikeResponse> toggleCommentLike(@UserId Long userId, @PathVariable Long postId, @PathVariable Long commentId) {
        boolean isLiked = likeService.toggleCommentLike(postId, commentId, userId);
        int like = likeService.getCommentLike(commentId);

        LikeResponse response = LikeResponse.builder()
                .isLiked(isLiked)
                .like(like)
                .build();

        return ResponseDto.ok(response);
    }
}


