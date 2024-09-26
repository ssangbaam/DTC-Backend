package hanium.dtc.domain.community.service;

import java.util.stream.Collectors;

import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.community.dto.response.PostDetailResponse;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.travel.domain.TravelRecord;
import hanium.dtc.domain.community.dto.request.PostRequest;
import hanium.dtc.domain.community.dto.response.CommentResponse;
import hanium.dtc.domain.travel.dto.response.TravelRecordResponse;
import hanium.dtc.domain.community.repository.PostRepository;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import hanium.dtc.domain.user.repository.UserRepository;
import hanium.dtc.domain.travel.repository.TravelRecordRepository;

import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TravelRecordRepository travelRecordRepository;

    @Transactional
    public boolean createPost(PostRequest postRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        TravelRecord travelRecord = null;
        if (postRequest.travelId() != null) {
            travelRecord = travelRecordRepository.findById(postRequest.travelId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        }
        Post post = new Post(postRequest.title(), postRequest.content(), travelRecord);
        post.setUser(user);
        postRepository.save(post);
        return true;
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        TravelRecordResponse travelRecordResponse = null;
        if (post.getTravelRecord() != null) {
            TravelRecord travelRecord = post.getTravelRecord();
            travelRecordResponse = TravelRecordResponse.builder()
                    .title(travelRecord.getTitle())
                    .place(travelRecord.getPlace())
                    .departAt(travelRecord.getDepartAt())
                    .arriveAt(travelRecord.getArriveAt())
                    .imageUrl(travelRecord.getImageUrl())
                    .travelRecordId(travelRecord.getId())
                    .build();
        }

        List<CommentResponse> comments = post.getComments().stream()
                .map(comment -> CommentResponse.builder()
                        .user(UserCommentResponse.builder()
                                .nickname(comment.getUser().getNickname())
                                .image(comment.getUser().getImage())
                                .build())
                        .content(comment.getContent())
                        .like(comment.getCommentLike())
                        .commentTime(comment.getCommentTime())
                        .isReply(comment.getIsReply())
                        .build())
                .collect(Collectors.toList());

        return PostDetailResponse.builder()
                .id(post.getId())
                .user(UserCommentResponse.builder()
                        .nickname(post.getUser().getNickname())
                        .image(post.getUser().getImage())
                        .build())
                .travel(travelRecordResponse)
                .title(post.getTitle())
                .content(post.getContent())
                .like(post.getPostLike())
                .comment(post.getComment())
                .scrap(post.getScrap())
                .postTime(post.getPostTime())
                .comments(comments)
                .build();
    }

    @Transactional
    public boolean updatePost(Long postId, PostRequest postRequest, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ROLE);
        }

        TravelRecord travelRecord = null;
        if (postRequest.travelId() != null) {
            travelRecord = travelRecordRepository.findById(postRequest.travelId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        }

        post.updatePost(postRequest.title(), postRequest.content(), travelRecord);
        return true;
    }

    @Transactional
    public boolean deletePost(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ROLE);
        }

        postRepository.delete(post);
        return true;
    }
}
