package hanium.dtc.domain.travel.service;

import hanium.dtc.domain.travel.domain.TemporaryPlace;
import hanium.dtc.domain.travel.repository.TemporaryTravelRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.domain.travel.domain.TemporaryTravel;
import hanium.dtc.domain.travel.dto.request.TemporaryTravelCreateRequest;
import hanium.dtc.domain.travel.repository.TemporaryPlaceRepository;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryTravelService {
    private final UserRepository userRepository;
    private final TemporaryTravelRepository temporaryTravelRepository;
    private final TemporaryPlaceRepository temporaryPlaceRepository;

    @Transactional
    public Boolean createTemporaryTravel(Long userId, TemporaryTravelCreateRequest temporaryTravelCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        temporaryTravelRepository.save(TemporaryTravel.builder()
                .departAt(temporaryTravelCreateRequest.departAt())
                .arriveAt(temporaryTravelCreateRequest.arriveAt())
                .user(user)
                .build());

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updatePersonNumber(Long userId, Integer person) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);
        temporaryTravel.updatePerson(person);

        return Boolean.TRUE;
    }

    @Transactional
    public void createTemporaryPlace(Long userId, String[] eachPlaceAndDescription) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        TemporaryTravel temporaryTravel = temporaryTravelRepository.findByUser(user);

        temporaryPlaceRepository.save(TemporaryPlace.builder()
                        .place(eachPlaceAndDescription[0])
                        .description(eachPlaceAndDescription[1])
                        .temporaryTravel(temporaryTravel)
                .build());
    }
}
