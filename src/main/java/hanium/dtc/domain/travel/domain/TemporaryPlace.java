package hanium.dtc.domain.travel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_place")
public class TemporaryPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "place")
    private String place;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private TemporaryTravel temporaryTravel;

    @Builder
    public TemporaryPlace(String place, String description, TemporaryTravel temporaryTravel) {
        this.place = place;
        this.description = description;
        this. temporaryTravel = temporaryTravel;
    }
}
