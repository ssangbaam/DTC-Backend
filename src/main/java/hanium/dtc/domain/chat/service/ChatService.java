package hanium.dtc.domain.chat.service;

import hanium.dtc.domain.chat.domain.ChatRecord;
import hanium.dtc.domain.chat.dto.request.ChatRecordRequest;
import hanium.dtc.domain.chat.dto.response.ChatDetailListResponse;
import hanium.dtc.domain.chat.dto.response.ChatDetailResponse;
import hanium.dtc.domain.chat.dto.response.ChatRecordListResponse;
import hanium.dtc.domain.chat.dto.response.ChatRecordResponse;
import hanium.dtc.domain.chat.handler.ChatRoom;
import hanium.dtc.domain.chat.repository.ChatDetailRepository;
import hanium.dtc.domain.chat.repository.ChatRecordRepository;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.domain.user.domain.User;
import hanium.dtc.domain.user.dto.Response.UserCommentResponse;
import hanium.dtc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ChatDetailRepository chatDetailRepository;
    private final Map<Long, ChatRoom> chatRoomMap = new HashMap<>();

    public Boolean createChatRecord(ChatRecordRequest chatRecordRequest) {
        log.info("createChatRoom method called with userId: {}, otherId: {}", chatRecordRequest.userId(), chatRecordRequest.otherId());
        User user = userRepository.findById(chatRecordRequest.userId()).orElseThrow(()
                -> new CommonException(ErrorCode.NOT_FOUND_USER));


        chatRecordRepository.save(ChatRecord.builder()
                .otherId(chatRecordRequest.otherId())
                .user(user).build());

        return Boolean.TRUE;
    }

    public User getOther(Long userId, Long chatRecordId) {
        // 채팅 기록 조회
        ChatRecord chatRecord = chatRecordRepository.findById(chatRecordId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CHAT_RECORD));

        // 상대방의 프로필 조회
        if (chatRecord.getUser().getId().equals(userId)) {
            // userId가 현재 채팅 기록의 사용자 ID와 일치하면 otherId의 프로필 반환
            return userRepository.findById(chatRecord.getOtherId())
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        } else if (chatRecord.getOtherId().equals(userId)) {
            // userId가 채팅 기록의 otherId와 일치하면 userId의 프로필 반환
            return chatRecord.getUser();
        } else {
            // userId가 이 채팅 기록에 포함되어 있지 않으면 예외 발생
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
    }

    public ChatRecordListResponse chatRecordList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_USER));

        ChatRecordListResponse chatRecordListResponse = ChatRecordListResponse.builder()
                .chatRecordResponses(chatRecordRepository.findAll()
                .stream().filter(chatRecord ->
                        chatRecord.getUser().getId().equals(userId) ||
                                chatRecord.getOtherId().equals(userId)
                )
                .map(chatRecord -> ChatRecordResponse.builder()
                        .userCommentResponse(new UserCommentResponse(
                                getOther(userId, chatRecord.getId()).getName()
                                , getOther(userId, chatRecord.getId()).getImage()))
                        .recentChat(chatRecord.getRecentChat())
                        .recentTime(chatRecord.getRecentTime())
                        .build())
                .toList()).build();
        return chatRecordListResponse;
    }

    public ChatDetailListResponse chatDetailList(Long userId,Long recordId){
        ChatRecord chatRecord = chatRecordRepository.findById(recordId).orElseThrow(()
                ->new CommonException(ErrorCode.NOT_FOUND_CHAT_RECORD));



        ChatDetailListResponse chatDetailListResponse = ChatDetailListResponse.builder()
                .chatDetailResponses(chatRecord.getChatDetails().stream()
                        .map(chatDetail -> ChatDetailResponse.builder()
                                .senderId(userId)
                                .content(chatDetail.getContent())
                                .chatTime(chatDetail.getChatTime())
                                .chatType(chatDetail.getChatType())
                                .build())
                        .toList()).build();
        return chatDetailListResponse;
    }

    public ChatRoom findRoomById(Long roomId) {
        return chatRoomMap.get(roomId);
    }

    public void addSessionToChatRecord(ChatRecord chatRecord, WebSocketSession session) {
        ChatRoom chatRoom = chatRoomMap.computeIfAbsent(chatRecord.getId()
                , id -> new ChatRoom(chatRecord.getId(), chatRecord.getUser().getName()));
        chatRoom.addSession(session);
    }

    public void removeSessionFromChatRecord(ChatRecord chatRecord, WebSocketSession session) {
        ChatRoom chatRoom = chatRoomMap.get(chatRecord.getId());
        if (chatRoom != null) {
            chatRoom.removeSession(session);
        }
    }

    public Set<WebSocketSession> getSessionsByChatRecord(ChatRecord chatRecord) {
        ChatRoom chatRoom = chatRoomMap.get(chatRecord.getId());
        return chatRoom != null ? chatRoom.getSessions() : Set.of();
    }

}
