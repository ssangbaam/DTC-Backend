package hanium.dtc.domain.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_recommend")
public class TemporaryRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "recommend", length = 1000)
    private String recommend;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private TemporaryTravel temporaryTravel;

    @Builder
    public TemporaryRecommend(String day, String recommend, TemporaryTravel temporaryTravel) {
        this.day = day;
        this.recommend = recommend;
        this. temporaryTravel = temporaryTravel;
    }

    public void updateRecommend(String recommend) {
        this.recommend = recommend;
    }
}
