package hanium.dtc.domain.travel.controller;

import hanium.dtc.annotation.UserId;
import hanium.dtc.global.ResponseDto;
import hanium.dtc.domain.travel.dto.request.TemporaryTravelCreateRequest;
import hanium.dtc.domain.travel.service.TemporaryTravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TemporaryTravelController {
    private final TemporaryTravelService temporaryTravelService;

    @PostMapping("/temp-place")
    public ResponseDto<?> createTemporaryTravel(
            @UserId Long userId,
            @RequestBody TemporaryTravelCreateRequest temporaryTravelCreateRequest) {
        return ResponseDto.created(temporaryTravelService.createTemporaryTravel(userId, temporaryTravelCreateRequest));
    }

    @PatchMapping("/temp-place")
    public ResponseDto<?> updateTravelPerson(
            @UserId Long userId,
            @RequestParam Integer person) {
        return ResponseDto.created(temporaryTravelService.updatePersonNumber(userId, person));
    }
}
