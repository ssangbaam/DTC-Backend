package hanium.dtc.domain.community.controller;

import hanium.dtc.domain.community.dto.response.HotResponse;
import hanium.dtc.domain.community.dto.response.PostResponse;
import hanium.dtc.domain.community.dto.response.PaginatedResponse;
import hanium.dtc.domain.community.service.CommunityService;
import hanium.dtc.global.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/free")
    public ResponseDto<PaginatedResponse<PostResponse>> getFreeBoardPosts(@RequestParam(defaultValue = "1") int page) {
        Page<PostResponse> postPage = communityService.getFreeBoardPosts(page);

        PaginatedResponse<PostResponse> paginatedResponse = new PaginatedResponse<>(
                postPage.getContent(),
                postPage.getNumber() + 1, // 현재 페이지 번호 (1-based)
                postPage.getTotalPages(),
                postPage.getTotalElements()
        );

        return ResponseDto.ok(paginatedResponse);
    }

    @GetMapping("/travel")
    public ResponseDto<PaginatedResponse<PostResponse>> getTravelBoardPosts(@RequestParam(defaultValue = "1") int page) {
        Page<PostResponse> postPage = communityService.getTravelBoardPosts(page);

        PaginatedResponse<PostResponse> paginatedResponse = new PaginatedResponse<>(
                postPage.getContent(),
                postPage.getNumber() + 1, // 현재 페이지 번호 (1-based)
                postPage.getTotalPages(),
                postPage.getTotalElements()
        );

        return ResponseDto.ok(paginatedResponse);
    }

    @GetMapping("/hot")
    public ResponseDto<PaginatedResponse<PostResponse>> getHotBoardPosts(@RequestParam(defaultValue = "1") int page)
    {
        Page<PostResponse> postPage = communityService.getHotBoardPosts(page);

        PaginatedResponse<PostResponse> paginatedResponse = new PaginatedResponse<>(
                postPage.getContent(),
                postPage.getNumber() + 1, // 현재 페이지 번호 (1-based)
                postPage.getTotalPages(),
                postPage.getTotalElements()
        );

        return ResponseDto.ok(paginatedResponse);
    }

    @GetMapping("/hot/top")
    public ResponseDto<List<HotResponse>> getTop3HotPosts() {
        List<HotResponse> top3Posts = communityService.getTop3HotPosts();
        return ResponseDto.ok(top3Posts);
    }

}
