package hanium.dtc.domain.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "chat_detail")
public class ChatDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "content")
    private String content;

    @Column(name = "chat_time")
    private LocalDateTime chatTime;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private ChatRecord chatRecord;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Builder
    public ChatDetail(ChatRecord chatRecord, Long senderId, String content, Boolean userSend, LocalDateTime chatTime, ChatType type) {
        this.chatRecord = chatRecord;
        this.senderId = senderId;
        this.content = content;
        this.chatTime = chatTime;
        this.chatType = type;
    }

}
