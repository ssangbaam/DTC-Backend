package hanium.dtc.domain.chat.domain;

import hanium.dtc.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_record")
public class ChatRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "other_id")
    private Long otherId;

    @Column(name = "recent_chat")
    private String recentChat;

    @Column(name = "recent_time")
    private LocalDateTime recentTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatDetail> chatDetails = new ArrayList<>();

    @Builder
    public ChatRecord(Long id, Long otherId, String recentChat, LocalDateTime recentTime, User user, List<ChatDetail> chatDetails) {
        this.id = id;
        this.otherId = otherId;
        this.recentChat = recentChat;
        this.recentTime = recentTime;
        this.user = user;
        this.chatDetails = chatDetails;
    }
}
