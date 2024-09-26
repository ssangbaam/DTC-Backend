package hanium.dtc.domain.community.controller;

import hanium.dtc.domain.community.service.CommentService;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.domain.community.dto.request.CommentRequest;
import hanium.dtc.domain.community.dto.response.CommentResponse;
import hanium.dtc.annotation.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseDto<?> createComment(@UserId Long userId, @PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        return ResponseDto.created(commentService.createComment(postId,  userId, commentRequest));
    }

    @PatchMapping("/{postId}/{commentId}")
    public ResponseDto<?> updateComment(@UserId Long userId,@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        return ResponseDto.created(commentService.updateComment(postId, commentId, userId, commentRequest));
    }

    @DeleteMapping("/{postId}/{commentId}")
    public ResponseDto<?> deleteComment(@UserId Long userId, @PathVariable Long postId, @PathVariable Long commentId) {
        return ResponseDto.created(commentService.deleteComment(postId, commentId, userId));
    }

    @GetMapping("/{postId}")
    public ResponseDto<List<List<CommentResponse>>> getCommentsInPost(@PathVariable Long postId) {
        List<List<CommentResponse>> comments = commentService.getCommentsInPost(postId);
        return ResponseDto.ok(comments);
    }
}




