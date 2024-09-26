package hanium.dtc.domain.user.domain;

import hanium.dtc.auth.dto.request.SignUpRequest;
import hanium.dtc.domain.community.domain.Comment;
import hanium.dtc.domain.community.domain.CommentLike;
import hanium.dtc.domain.community.domain.Post;
import hanium.dtc.domain.community.domain.PostLike;
import hanium.dtc.domain.user.dto.Request.MyPageUpdateRequest;
import hanium.dtc.domain.travel.domain.TemporaryTravel;
import hanium.dtc.domain.travel.domain.TravelRecord;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_id")
    private Long serialId;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "mbti")
    private Integer mbti;

    @Column(name = "image")
    private Integer image;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TravelRecord> travelRecords = new ArrayList<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private TemporaryTravel temporaryTravel;

    public User(Long serialId) {
        this.serialId = serialId;
    }

    public void update(MyPageUpdateRequest request) {
        this.name = request.name();
        this.nickname = request.nickname();
        this.address = request.address();
        this.age = request.age();
        this.gender = request.gender();
        this.mbti = request.mbti();
    }

    public void signUpUser(SignUpRequest signUpRequest) {
        this.name = signUpRequest.name();
        this.nickname = signUpRequest.nickname();
        this.age = signUpRequest.age();
        this.address = signUpRequest.address();
        this.gender = signUpRequest.gender();
        this.mbti = signUpRequest.mbti();
        this.image = signUpRequest.image();
    }
}
