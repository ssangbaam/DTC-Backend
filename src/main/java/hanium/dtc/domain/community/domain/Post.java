package hanium.dtc.domain.community.domain;

import hanium.dtc.domain.travel.domain.TravelRecord;
import hanium.dtc.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "post_like")
    private Integer postLike = 0;

    @Column(name = "comment")
    private Integer comment;

    @Column(name = "scrap")
    private Integer scrap = 0;

    @Column(name = "is_travel")
    private Boolean isTravel;

    @Column(name = "post_time")
    private LocalDateTime postTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private TravelRecord travelRecord;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, TravelRecord travelRecord) {
        this.title = title;
        this.content = content;
        this.travelRecord = travelRecord;
        this.isTravel = (travelRecord != null); // travelRecord가 있으면 isTravel을 true로 설정
        this.postTime = LocalDateTime.now();
        this.postLike = 0;
        this.comment = 0;
        this.scrap = 0;
    }

    public void updatePost(String title, String content, TravelRecord travelRecord) {
        this.title = title;
        this.content = content;
        this.travelRecord = travelRecord;
        this.isTravel = (travelRecord != null);
    }

    public void incrementLike() {
        this.postLike += 1;
    }

    public void decrementLike() {
        if (this.postLike > 0) {
            this.postLike -= 1;
        }
    }

    public void incrementScrap() {
        this.scrap += 1;
    }

    public void decrementScrap() {
        if (this.scrap > 0) {
            this.scrap -= 1;
        }
    }
}
