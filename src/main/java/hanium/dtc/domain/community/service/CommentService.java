package hanium.dtc.domain.community.service;

import hanium.dtc.domain.community.domain.Comment;
import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.community.dto.request.CommentRequest;
import hanium.dtc.domain.community.dto.response.CommentResponse;
import hanium.dtc.domain.community.repository.CommentRepository;
import hanium.dtc.domain.community.repository.PostRepository;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import hanium.dtc.domain.user.repository.UserRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean createComment(Long postId, Long userId, CommentRequest commentRequest) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Comment comment = new Comment(
                commentRequest.content(),
                commentRequest.isReply(),
                commentRequest.commentId(),
                post
        );
        comment.setUser(user);
        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public boolean updateComment(Long postId, Long commentId, Long userId, CommentRequest commentRequest) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ROLE);
        }

        comment.updateContent(commentRequest.content());
        commentRepository.save(comment);
        return true;
    }

    @Transactional
    public boolean deleteComment(Long postId, Long commentId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CommonException(ErrorCode.FORBIDDEN_ROLE);
        }

        commentRepository.delete(comment);
        return true;
    }

    @Transactional(readOnly = true)
    public List<List<CommentResponse>> getCommentsInPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        // 부모 댓글과 자식 댓글을 그룹핑할 맵을 선언
        Map<Long, List<CommentResponse>> groupedComments = new HashMap<>();

        List<Comment> comments = commentRepository.findByPostId(postId).orElse(Collections.emptyList());

        comments.forEach(comment -> {
            UserCommentResponse userCommentResponse = UserCommentResponse.builder()
                    .nickname(comment.getUser().getNickname())
                    .image(comment.getUser().getImage())
                    .build();

            CommentResponse response = CommentResponse.builder()
                    .content(comment.getContent())
                    .user(userCommentResponse)
                    .like(comment.getCommentLike())
                    .commentTime(comment.getCommentTime())
                    .isReply(comment.getIsReply())
                    .build();

            if (!comment.getIsReply()) {
                // 부모 댓글인 경우 새로운 리스트에 추가
                groupedComments.put(comment.getId(), new ArrayList<>(List.of(response)));
            } else {
                // 자식 댓글인 경우 부모 댓글 리스트에 추가
                groupedComments.computeIfPresent(comment.getCommentId(), (key, list) -> {
                    list.add(response);
                    return list;
                });
            }
        });

        // 최종 결과로 이중 배열을 반환
        List<List<CommentResponse>> result = new ArrayList<>();

        // 부모 댓글이 있는 맵을 순회하면서 자식 댓글도 포함하여 리스트로 추가
        groupedComments.forEach((parentId, commentList) -> {
            List<CommentResponse> nestedComments = new ArrayList<>();
            nestedComments.addAll(commentList);
            result.add(nestedComments);
        });

        return result;
    }


}