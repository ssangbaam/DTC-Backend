package hanium.dtc.domain.travel.service;

import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.community.repository.PostRepository;
import hanium.dtc.domain.travel.domain.TravelRecord;
import hanium.dtc.domain.travel.dto.response.*;
import hanium.dtc.domain.travel.repository.TravelRecordRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.domain.travel.domain.RecordDetail;
import hanium.dtc.domain.travel.dto.request.TravelRecordDetailRequest;
import hanium.dtc.domain.travel.dto.request.TravelTitleRequest;
import hanium.dtc.domain.travel.repository.RecordDetailRepository;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelRecordService {
    private final TravelRecordRepository travelRecordRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public TravelRecordListResponse travelRecordList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        LocalDate filterDate = LocalDate.now();

        TravelRecordListResponse travelRecordListResponse = TravelRecordListResponse.builder()
                .travelRecordResponses(user.getTravelRecords().stream()
                        .filter(travelRecord -> travelRecord.getDepartAt().isBefore(filterDate)
                                && travelRecord.getIsScrap().equals(Boolean.FALSE))
                        .map(travelRecord -> TravelRecordResponse.builder()
                                .title(travelRecord.getTitle().toString())
                                .place(travelRecord.getPlace().toString())
                                .departAt(travelRecord.getDepartAt())
                                .arriveAt(travelRecord.getArriveAt())
                                .imageUrl(travelRecord.getImageUrl())
                                .travelRecordId(travelRecord.getId())
                                .build())
                        .toList()).build();
        return travelRecordListResponse;
    }

    @Transactional(readOnly = true)
    public TravelRecordListResponse travelPlanList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        LocalDate filterDate = LocalDate.now();

        TravelRecordListResponse travelRecordListResponse = TravelRecordListResponse.builder()
                .travelRecordResponses(user.getTravelRecords().stream()
                        .filter(travelRecord -> travelRecord.getDepartAt().isAfter(filterDate)
                                && travelRecord.getIsScrap().equals(Boolean.FALSE))
                        .map(travelRecord -> TravelRecordResponse.builder()
                                .travelRecordId(travelRecord.getId())
                                .title(travelRecord.getTitle().toString())
                                .place(travelRecord.getPlace().toString())
                                .departAt(travelRecord.getDepartAt())
                                .arriveAt(travelRecord.getArriveAt())
                                .imageUrl(travelRecord.getImageUrl())
                                .build())
                        .toList()).build();
        return travelRecordListResponse;
    }

    @Transactional(readOnly = true)
    public List<TravelRecordResponse> getMyScrapList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return travelRecordRepository.findByUser(user).stream()
                .filter(travelRecord -> travelRecord.getIsScrap().equals(Boolean.TRUE))
                .map(travelRecord -> TravelRecordResponse.builder()
                        .travelRecordId(travelRecord.getId())
                        .title(travelRecord.getTitle())
                        .place(travelRecord.getPlace())
                        .departAt(travelRecord.getDepartAt())
                        .arriveAt(travelRecord.getArriveAt())
                        .imageUrl(travelRecord.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetailInfoResponse getDetailInfo(Long userId, Long detailId) {
        RecordDetail recordDetail = recordDetailRepository.findById(detailId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        return DetailInfoResponse.builder()
                .title(recordDetail.getTitle())
                .thema(recordDetail.getThema())
                .address(recordDetail.getDetailAddress())
                .lat(recordDetail.getLat())
                .lon(recordDetail.getLon()).build();
    }

    @Transactional(readOnly = true)
    public TravelRecordDetailResponse travelRecordDetail(Long travelId, Integer day) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));
        List<RecordDetail> recordDetails = recordDetailRepository.findByTravelRecordAndDay(travelRecord, day)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        LocalDate today = travelRecord.getDepartAt().plusDays(day - 1);
        ArrayList<DateResponse> dateResponses = new ArrayList<>();
        // 3일 전부터 3일 후까지의 날짜와 요일 출력
        for (int i = -3; i <= 3; i++) {
            LocalDate targetDate = today.plusDays(i);
            String dayOfWeek = targetDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            Integer dayOfMonth = targetDate.getDayOfMonth();

            String dday;
            Integer dayInt;
            Integer plusDay = day + i;
            if(targetDate.isBefore(travelRecord.getDepartAt()) || targetDate.isAfter(travelRecord.getArriveAt())) {
                dday = null;
                dayInt = -1;
            } else {
                dday = "Day" + " " + plusDay;
                dayInt = day + i;
            }

            dateResponses.add(DateResponse.builder()
                            .date(dayOfMonth)
                            .day(dayOfWeek)
                            .dday(dday)
                            .dayInt(dayInt)
                    .build());
        }

        TravelRecordDetailResponse travelRecordDetailResponse = TravelRecordDetailResponse.builder()
                .dateResponses(dateResponses)
                .travelDetailResponse(TravelDetailResponse.builder()
                        .title(travelRecord.getTitle())
                        .departAt(travelRecord.getDepartAt())
                        .arriveAt(travelRecord.getArriveAt())
                        .build())
                .recordDetailResponses(recordDetails.stream()
                        .map(RecordDetail-> RecordDetailResponse.builder()
                                .id(RecordDetail.getId())
                                .title(RecordDetail.getTitle())
                                .thema(RecordDetail.getThema())
                                .detailAddress(RecordDetail.getDetailAddress())
                                .plan(RecordDetail.getReview())
                                .startAt(RecordDetail.getStartAt())
                                .endAt(RecordDetail.getEndAt())
                                .lat(RecordDetail.getLat())
                                .lon(RecordDetail.getLon())
                                .build())
                        .toList())
                .build();
        return travelRecordDetailResponse;
    }

    @Transactional
    public ScrapResponse toggleScrapTravelRecord(Long postId, Long travelId, Long userId){

        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        Optional<TravelRecord> existingScrap = travelRecordRepository.findByUserAndId(user, travelId);

        boolean isScrapped;
        if (existingScrap.isPresent()) {
            travelRecordRepository.delete(existingScrap.get());
            post.decrementScrap();
            isScrapped = false;
        } else {
            TravelRecord newTravelRecord = new TravelRecord(
                    travelRecord.getTitle(),
                    travelRecord.getPlace(),
                    travelRecord.getDepartAt(),
                    travelRecord.getArriveAt(),
                    travelRecord.getImageUrl(),
                    user
            );

            for (RecordDetail recordDetails : travelRecord.getRecordDetails()){
                RecordDetail newDetail = new RecordDetail(
                        recordDetails.getTitle(),
                        recordDetails.getThema(),
                        recordDetails.getDetailAddress(),
                        recordDetails.getLat(),
                        recordDetails.getLon(),
                        recordDetails.getStartAt(),
                        recordDetails.getEndAt(),
                        recordDetails.getDay(),
                        newTravelRecord
                );
                newTravelRecord.getRecordDetails().add(newDetail);
            }
            travelRecordRepository.save(newTravelRecord);
            post.incrementScrap();
            isScrapped = true;
        }

        postRepository.save(post);

        return ScrapResponse.builder()
                .isScrapped(isScrapped)
                .scrap(post.getScrap())
                .build();
    }

    @Transactional
    public ScrapResponse removeScrap(Long postId, Long travelId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        TravelRecord travelRecord = travelRecordRepository.findByUserAndId(user, travelId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_POST));

        travelRecordRepository.delete(travelRecord);
        post.decrementScrap();

        postRepository.save(post);

        return ScrapResponse.builder()
                .isScrapped(false)
                .scrap(post.getScrap())
                .build();
    }

    @Transactional
    public boolean updateTravelTitle(Long travelId, TravelTitleRequest request) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        travelRecord.updateTitle(request.title());
        travelRecordRepository.save(travelRecord);
        return Boolean.TRUE;
    }

    @Transactional
    public boolean updateRecordDetail(Long detailId, TravelRecordDetailRequest request) {
        RecordDetail recordDetail = recordDetailRepository.findById(detailId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        recordDetail.updateDetail(
                request.title(),
                request.thema(),
                request.detailAddress(),
                request.startAt(),
                request.endAt()
        );

        recordDetailRepository.save(recordDetail);
        return Boolean.TRUE;
    }


    @Transactional
    public boolean deleteRecordDetail(Long detailId) {
        RecordDetail recordDetail = recordDetailRepository.findById(detailId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVELDETAIL));

        recordDetailRepository.delete(recordDetail);
        return Boolean.TRUE;
    }

    @Transactional
    public boolean deleteTravelRecord(Long travelId) {
        TravelRecord travelRecord = travelRecordRepository.findById(travelId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_TRAVEL));

        travelRecordRepository.delete(travelRecord);
        return Boolean.TRUE;
    }

}

