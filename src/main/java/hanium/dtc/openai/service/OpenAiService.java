package hanium.dtc.openai.service;

import hanium.dtc.domain.travel.repository.*;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.openai.dto.response.*;
import hanium.dtc.openai.prompt.*;
import hanium.dtc.openai.dto.request.OpenAiRequest;
import hanium.dtc.domain.travel.domain.RecordDetail;
import hanium.dtc.domain.travel.domain.TemporaryRecommend;
import hanium.dtc.domain.travel.domain.TemporaryTravel;
import hanium.dtc.domain.travel.domain.TravelRecord;
import hanium.dtc.domain.travel.service.TemporaryTravelService;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.model}")
    private String openAiModel;
    private final RestClient openAiRestClient;
    private final TravelRecommendationPrompt travelRecommendationPrompt;
    private final TravelDescriptionPrompt travelDescriptionPrompt;
    private final TravelTendencyPrompt travelTendencyPrompt;
    private final TravelTimetablePrompt travelTimetablePrompt;
    private final SelectNextStepPrompt selectNextStepPrompt;
    private final TravelPlanInfoPrompt travelPlanInfoPrompt;
    private final TravelAddressPrompt travelAddressPrompt;
    private final TravelCheckDayPrompt travelCheckDayPrompt;
    private final ResponseHandleService responseHandleService;
    private final TemporaryTravelService temporaryTravelService;
    private final UserRepository userRepository;
    private final TravelRecordRepository travelRecordRepository;
    private final RecordDetailRepository recordDetailRepository;
    private final TemporaryTravelRepository temporaryTravelRepository;
    private final TemporaryPlaceRepository temporaryPlaceRepository;
    private final TemporaryRecommendRepository temporaryRecommendRepository;

    public String getTravelRecommendation(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelRecommendationPrompt.getRoles(), travelRecommendationPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody()
                .openAiChoices().get(0)
                .openAiResponseMessage().content();
    }

    public OpenAiResponse getTravelDescription(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelDescriptionPrompt.getRoles(), travelDescriptionPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelTendency(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelTendencyPrompt.getRoles(), travelTendencyPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelTimeTable(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelTimetablePrompt.getRoles(), travelTimetablePrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getNextStep(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, selectNextStepPrompt.getRoles(), selectNextStepPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelPlanInfo(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelPlanInfoPrompt.getRoles(), travelPlanInfoPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelAddress(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelAddressPrompt.getRoles(), travelAddressPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    public OpenAiResponse getTravelDay(String userRequest) {
        return openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(OpenAiRequest.of(openAiModel, travelCheckDayPrompt.getRoles(), travelCheckDayPrompt.getContents(), userRequest, 0.5))
                .retrieve()
                .toEntity(OpenAiResponse.class)
                .getBody();
    }

    @Transactional
    public void setTravelState(Long userId, String place, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        temporaryTravel.updatePlace(place);
        temporaryTravel.updateTendency(userRequest);
        temporaryTravel.nextStep();
    }

    // 임시 여행 기록을 여행 기록으로 저장
    @Transactional
    public Long saveTravelRecord(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        // 여행 계획이 확정이니까 임시 여행기록에 있던 데이터들을 여행기록으로 전환
        TravelRecord travelRecord = new TravelRecord(temporaryTravel);
        travelRecordRepository.save(travelRecord);

        List<String> recommends = temporaryRecommendRepository.findAllRecommendByTemporaryTravel(temporaryTravel);
        List<String> subRecommends = recommends.subList(0, recommends.size()-1);

        Integer eachDay = 1;
        for(String recommend : subRecommends) {
            List<String> RecordDetailOfDays = responseHandleService.parseEachTimeOfDay(recommend);
            LocalDate today = travelRecord.getDepartAt().plusDays(eachDay - 1);
            for (String eachRecordDetail : RecordDetailOfDays) {
                String time = responseHandleService.parseTimeAndSchedule(eachRecordDetail).get(0).trim();
                String planOfTime = responseHandleService.parseTimeAndSchedule(eachRecordDetail).get(1).trim();
                String plan = responseHandleService
                        .convertOpenAiResponseToString(getTravelPlanInfo(planOfTime + " & " + travelRecord.getPlace()));
                String planCopy = new String(plan);
                List<String> planInfoOfTime = responseHandleService.parsePlaceAndThemaAndActive(planCopy);

                final int PLACE = 0;
                final int THEMA = 1;
                final int PLAN = 2;
                final int ADDRESS = 3;
                final int LAT = 4;
                final int LON = 5;

                if (!planInfoOfTime.get(THEMA).equals("숙박") && !planInfoOfTime.get(THEMA).equals("식당")) {
                    final int ADDRESS_ = 0;
                    final int LAT_ = 1;
                    final int LON_ = 2;

                    List<String> planAddress = responseHandleService.parsePlaceAndThemaAndActive(
                            responseHandleService.convertOpenAiResponseToString(
                                    getTravelAddress(
                                            travelRecord.getPlace() + " " + planInfoOfTime.get(PLACE))));

                    if (!planAddress.get(ADDRESS_).equals("0")) {
                        plan = plan + "^" + planAddress.get(ADDRESS_) + "^" + planAddress.get(LAT_) + "^" + planAddress.get(LON_);
                    } else {
                        plan = plan + "^" + "-";
                    }
                } else {
                    plan = plan + "^" + "-";
                }

                planInfoOfTime = responseHandleService.parsePlaceAndThemaAndActive(plan);

                if (!planInfoOfTime.get(ADDRESS).equals("-")) {
                    RecordDetail recordDetail = RecordDetail.builder()
                            .title(planInfoOfTime.get(PLACE))
                            .thema(planInfoOfTime.get(THEMA))
                            .detailAddress(planInfoOfTime.get(ADDRESS))
                            .lat(Double.parseDouble(planInfoOfTime.get(LAT)))
                            .lon(Double.parseDouble(planInfoOfTime.get(LON)))
                            .startAt(LocalDateTime.of(today, LocalTime.parse(time)))
                            .endAt(LocalDateTime.of(today, LocalTime.parse(time)))
                            .day(eachDay)
                            .review(planInfoOfTime.get(PLAN))
                            .travelRecord(travelRecord)
                            .build();

                    recordDetailRepository.save(recordDetail);
                } else {
                    RecordDetail recordDetail = RecordDetail.builder()
                            .title(planInfoOfTime.get(PLACE))
                            .thema(planInfoOfTime.get(THEMA))
                            .detailAddress("-")
                            .lat(null)
                            .lon(null)
                            .startAt(LocalDateTime.of(today, LocalTime.parse(time)))
                            .endAt(LocalDateTime.of(today, LocalTime.parse(time)))
                            .day(eachDay)
                            .review(planInfoOfTime.get(PLAN))
                            .travelRecord(travelRecord)
                            .build();

                    recordDetailRepository.save(recordDetail);
                }
            }
            eachDay++;
        }
        return travelRecord.getId();
    }

    // STEP 1
    @Transactional
    public TravelListResponse getListOfTravel(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        String userTendency = responseHandleService.convertOpenAiResponseToString(getTravelTendency(userRequest));
        String[] gptResponseList =  responseHandleService.parseFirstElement(getTravelRecommendation(userRequest));
        String place = gptResponseList[0];
        String[] getDescriptionAndPlace = responseHandleService.parseFirstElement(gptResponseList[1]);
        String placeDescription =  getDescriptionAndPlace[0];
        String placeList = getDescriptionAndPlace[1];
        String placeListWithDescription = responseHandleService.convertOpenAiResponseToString(getTravelDescription(placeList));
        String[] listOfEachPlace = responseHandleService.parseEachTravelPlace(placeListWithDescription);

        setTravelState(userId, place, userTendency);
        for(String eachPlace : listOfEachPlace) {
            try {
                String[] eachPlaceAndDescription = responseHandleService.parsePlaceAndDscription(eachPlace);
                temporaryTravelService.createTemporaryPlace(userId, eachPlaceAndDescription);
            } catch (Exception e) {
                throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        return TravelListResponse.builder()
                .step(1)
                .description(placeDescription)
                .placeDescriptionResponses(user.getTemporaryTravel().getTemporaryPlaces().stream()
                        .map(temporaryTravel ->
                                PlaceDescriptionResponse.builder()
                                        .id(temporaryTravel.getId())
                                        .place(temporaryTravel.getPlace())
                                        .description(temporaryTravel.getDescription())
                                        .build())
                        .toList())
                .build();
    }

    // STEP 2
    @Transactional
    public TravelRecommendResponse createTravelTimeTable(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);
        List<Long> preferPlaces = responseHandleService.parsePreferPlace(userRequest);
        Long day = ChronoUnit.DAYS.between(temporaryTravel.getDepartAt(), temporaryTravel.getArriveAt());
        Integer dayBetween = day.intValue() + 1;

        String placeString = "8.선호하는 여행지 목록 : ";
        for(Long preferplace : preferPlaces) {
            placeString += temporaryPlaceRepository.findById(preferplace)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_TEMP_PLACE))
                    .getPlace() + ", ";
        }
        placeString += responseHandleService.dateToDateString(dayBetween); // 여행 날짜 추가
        placeString += "10.인원 : " + temporaryTravel.getPerson().toString();
        placeString = temporaryTravel.getTendency() + placeString;

        List<String> timeTable = responseHandleService
                .parseTimeTable(responseHandleService
                        .convertOpenAiResponseToString(getTravelTimeTable(placeString)));

        ArrayList<TravelEachRecommend> travelEachRecommends = new ArrayList<>();
        for(String eachTime : timeTable) {
            TemporaryRecommend temporaryRecommend = TemporaryRecommend.builder()
                    .day(responseHandleService.parseTimeAndTravel(eachTime).get(0).trim())
                    .recommend(responseHandleService.parseTimeAndTravel(eachTime).get(1).trim())
                    .temporaryTravel(temporaryTravel)
                    .build();
            temporaryRecommendRepository.save(temporaryRecommend);

            travelEachRecommends.add(TravelEachRecommend.builder()
                    .id(temporaryRecommend.getId())
                    .day(temporaryRecommend.getDay())
                    .plan(temporaryRecommend.getRecommend())
                    .build());
        }
        temporaryTravel.nextStep();
        travelEachRecommends.remove(travelEachRecommends.size() - 1);

        return TravelRecommendResponse.builder()
                .step(2)
                .travelEachRecommends(travelEachRecommends)
                .build();
    }

    // STEP 3
    @Transactional
    public Object fixTravelTimeTable(Long userId, String userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        if(responseHandleService.convertOpenAiResponseToString(getNextStep(userRequest)).equals("0")) {
            // 만약 더 이상 여행 일정을 수정하지 않는다면 다음과 같은 로직 진행
            Long travelId = saveTravelRecord(userId);
            temporaryTravel.nextStep();

            List<TemporaryRecommend> temporaryRecommends = temporaryRecommendRepository.findAll();
            List<TemporaryRecommend> temporarySubRecommends = temporaryRecommends.subList(0, temporaryRecommends.size()-1);
            temporaryTravelRepository.delete(temporaryTravel);
            return TravelListFinalResponse.builder()
                    .step(temporaryTravel.getQuestionStep())
                    .message("최종 여행 일정은 다음과 같습니다.")
                    .travelId(travelId)
                    .travelEachRecommends(temporarySubRecommends.stream().map(
                                    Recommend ->
                                            TravelEachRecommend.builder()
                                                    .id(Recommend.getId())
                                                    .day(Recommend.getDay())
                                                    .plan(Recommend.getRecommend()).build())
                            .toList())
                    .build();
        } else {
            // 여행 일정을 수정하고자 한다면 다음과 같은 로직 진행
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String schedule = "(여행 일정 : " + temporaryTravel.getDepartAt().format(formatter) + " ~ " + temporaryTravel.getArriveAt().format(formatter) + ") " + userRequest;

            TemporaryRecommend temporaryRecommend = temporaryRecommendRepository
                    .findByDayAndTemporaryTravel(
                            responseHandleService.convertOpenAiResponseToString(getTravelDay(schedule)),
                            temporaryTravel
                    ).get(0);

            String reRecommend = responseHandleService.convertOpenAiResponseToString(
                    getNextStep(userRequest + temporaryRecommend.getRecommend())
            );
            temporaryRecommend.updateRecommend(reRecommend);

            List<TemporaryRecommend> temporaryRecommends = temporaryRecommendRepository.findAll();
            List<TemporaryRecommend> temporarySubRecommends = temporaryRecommends.subList(0, temporaryRecommends.size()-1);
            return TravelListFixResponse.builder()
                    .step(temporaryTravel.getQuestionStep())
                    .message("수정된 여행일정은 다음과 같습니다.")
                    .travelEachRecommends(temporarySubRecommends.stream().map(
                            Recommend ->
                                    TravelEachRecommend.builder()
                                            .id(Recommend.getId())
                                            .day(Recommend.getDay())
                                            .plan(Recommend.getRecommend()).build())
                            .toList())
                    .build();
        }

    }

    @Transactional
    public Object getOpenAiResponse(Long userId, String userRequest) throws RuntimeException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        switch (temporaryTravelRepository.findByUser(user).getQuestionStep()) {
            case 1: // 첫 질문에 대한 유저의 응답
                return getListOfTravel(userId, userRequest);
            case 2: // 다음 스탭
                return createTravelTimeTable(userId, userRequest);
            case 3:
                return fixTravelTimeTable(userId, userRequest);
            default:
                break;
        }

        return new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
