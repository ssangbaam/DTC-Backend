package hanium.dtc.domain.travel.controller;

import hanium.dtc.annotation.UserId;
import hanium.dtc.domain.travel.dto.response.ScrapResponse;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.domain.travel.dto.request.TravelRecordDetailRequest;
import hanium.dtc.domain.travel.dto.request.TravelTitleRequest;
import hanium.dtc.domain.travel.service.TravelRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TravelRecordController {
    private final TravelRecordService travelRecordService;

    @PostMapping("/mypage/record/scrap/{postId}/{travelId}")
    public ResponseDto<ScrapResponse> toggleScrap(@PathVariable Long postId, @PathVariable Long travelId, @UserId Long userId) {
        ScrapResponse response = travelRecordService.toggleScrapTravelRecord(postId ,travelId, userId);
        return ResponseDto.ok(response);
    }

    @GetMapping("/mypage/record")
    public ResponseDto<?> getTravelRecord(@UserId Long userId) {
        return ResponseDto.ok(travelRecordService.travelRecordList(userId));
    }

    @GetMapping("/mypage/plan")
    public ResponseDto<?> getTravelPlan(@UserId Long userId) {
        return ResponseDto.ok(travelRecordService.travelPlanList(userId));
    }

    @GetMapping("/mypage/scrap")
    public ResponseDto<Object> getMyScrapList(@UserId Long userId) {
        return ResponseDto.ok(travelRecordService.getMyScrapList(userId));
    }

    @GetMapping("/record/{detailId}")
    public ResponseDto<?> getDetailInfo(@UserId Long userId, @PathVariable Long detailId) {
        return ResponseDto.ok(travelRecordService.getDetailInfo(userId, detailId));
    }

    @GetMapping("/mypage/{travelId}")
    public ResponseDto<?> getTravelDetail(
            @PathVariable Long travelId,
            @RequestParam(value = "day", required = false, defaultValue = "1") Integer day) {
        return ResponseDto.ok(travelRecordService.travelRecordDetail(travelId, day));
    }

    @DeleteMapping("/mypage/scrap/{postId}/{travelId}")
    public ResponseDto<ScrapResponse> removeScrap(@PathVariable Long postId, @PathVariable Long travelId,  @UserId Long userId) {
        ScrapResponse response = travelRecordService.removeScrap(postId, travelId, userId);
        return ResponseDto.ok(response);
    }

    @PatchMapping("/mypage/{travelId}")
    public ResponseDto<?> updateTravelTitle(@PathVariable Long travelId, @RequestBody TravelTitleRequest request) {
        return ResponseDto.created(travelRecordService.updateTravelTitle(travelId, request));
    }

    @PatchMapping("/mypage/detail/{detailId}")
    public ResponseDto<?> updateRecordDetail(@PathVariable Long detailId, @RequestBody TravelRecordDetailRequest request) {
        return ResponseDto.created(travelRecordService.updateRecordDetail(detailId, request));
    }

    @DeleteMapping("mypage/detail/{detailId}")
    public ResponseDto<?> deleteRecordDetail(@PathVariable Long detailId) {
        return ResponseDto.created(travelRecordService.deleteRecordDetail(detailId));
    }

    @DeleteMapping("mypage/record/{travelId}")
    public ResponseDto<?> deleteTravelRecord(@PathVariable Long travelId) {
        return ResponseDto.created(travelRecordService.deleteTravelRecord(travelId));
    }

    @DeleteMapping("mypage/plan/{travelId}")
    public ResponseDto<?> deleteTravelPlan(@PathVariable Long travelId) {
        return ResponseDto.created(travelRecordService.deleteTravelRecord(travelId));
    }
/*
    @DeleteMapping("mypage/scrap/{travelId}")
    public ResponseDto<?> deleteTravelScrap(@PathVariable Long travelId) {
        return ResponseDto.created(travelRecordService.deleteTravelRecord(travelId));
    }

 */

}

