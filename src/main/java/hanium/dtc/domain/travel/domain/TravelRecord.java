package hanium.dtc.domain.travel.domain;

import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel_record")
public class TravelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "place")
    private String place;

    @Column(name = "depart_at")
    private LocalDate departAt;

    @Column(name = "arrive_at")
    private LocalDate arriveAt;

    @Column(name = "image_url", length = 1200)
    private String imageUrl;

    @Setter
    @Column(name = "is_scrap")
    private Boolean isScrap;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "travelRecord", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "travelRecord", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordDetail> recordDetails = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public TravelRecord(String title, String place, LocalDate departAt, LocalDate arriveAt, String imageUrl, User user) {
        this.title = title;
        this.place = place;
        this.departAt = departAt;
        this.arriveAt = arriveAt;
        this.imageUrl = imageUrl;
        this.user = user;
        this.isScrap = true;
        this.posts = null;
    }

    public TravelRecord(TemporaryTravel temporaryTravel) {
        this.title = null;
        this.place = temporaryTravel.getPlace();
        this.departAt = temporaryTravel.getDepartAt();
        this.arriveAt = temporaryTravel.getArriveAt();
        this.imageUrl = "https://i2.wp.com/www.agoda.com/wp-content/uploads/2021/02/Donggung-Palace-and-Wolji-Pond-Gyeongju-si-attractions-South-Korea.jpg?ssl=1";
        this.isScrap = false;
        this.user = temporaryTravel.getUser();
        this.posts = null;
        this.recordDetails = null;
    }
}

