package hanium.dtc.domain.travel.domain;

import hanium.dtc.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_travel")
public class TemporaryTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "depart_at")
    private LocalDate departAt;

    @Column(name = "arrive_at")
    private LocalDate arriveAt;

    @Column(name = "person")
    private Integer person;

    @Column(name = "place")
    private String place;

    @Column(name = "tendency")
    private String tendency;

    @Column(name = "question_step")
    private Integer questionStep;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "temporaryTravel", fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<TemporaryPlace> temporaryPlaces = new ArrayList<>();

    @OneToMany(mappedBy = "temporaryTravel", fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<TemporaryRecommend> temporaryRecommends = new ArrayList<>();

    @Builder
    public TemporaryTravel(LocalDate departAt, LocalDate arriveAt, User user) {
        this.departAt = departAt;
        this.arriveAt = arriveAt;
        this.user = user;
        this.temporaryPlaces = null;
        this.tendency = "";
        this.questionStep = 1;
    }

    public void updatePerson(Integer person) {
        this.person = person;
    }

    public void updateTendency(String tendency) {
        this.tendency = tendency;
    }

    public void updatePlace(String place) {
        this.place = place;
    }

    public void nextStep() {
        this.questionStep += 1;
    }
}
