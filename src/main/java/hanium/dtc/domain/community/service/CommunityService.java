package hanium.dtc.domain.community.service;

import hanium.dtc.domain.community.dto.response.PostResponse;
import hanium.dtc.domain.community.dto.response.HotResponse;
import hanium.dtc.domain.community.repository.PostRepository;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostResponse> getFreeBoardPosts(int page) {
        int size = 10;
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "postTime"));
        return postRepository.findByIsTravel(false, pageable)
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .user(UserCommentResponse.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getTravelBoardPosts(int page) {
        int size = 8;
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "postTime"));
        return postRepository.findByIsTravel(true, pageable)
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .user(UserCommentResponse.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getHotBoardPosts(int page) {
        int size = 10;
        int likeThreshold = 10;
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "postTime"));
        return postRepository.findByPostLikeGreaterThanEqual(likeThreshold, pageable)
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .user(UserCommentResponse.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .like(post.getPostLike())
                        .comment(post.getComment())
                        .scrap(post.getScrap())
                        .postTime(post.getPostTime())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<HotResponse> getTop3HotPosts() {
        int topSize = 3;
        PageRequest pageable = PageRequest.of(0, topSize, Sort.by(Sort.Direction.DESC, "postLike")); // 페이지 번호 수정
        return postRepository.findByIsTravel(true, pageable).stream()
                .map(post -> HotResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .like(post.getPostLike())
                        .user(UserCommentResponse.builder()
                                .nickname(post.getUser().getNickname())
                                .image(post.getUser().getImage())
                                .build())
                        .place(post.getTravelRecord().getPlace())
                        .imageUrl(post.getTravelRecord().getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

}

