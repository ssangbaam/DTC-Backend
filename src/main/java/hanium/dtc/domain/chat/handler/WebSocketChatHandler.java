package hanium.dtc.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanium.dtc.domain.chat.domain.ChatDetail;
import hanium.dtc.domain.chat.domain.ChatRecord;
import hanium.dtc.domain.chat.dto.request.ChatDetailRequest;
import hanium.dtc.domain.chat.dto.response.ChatDetailResponse;
import hanium.dtc.domain.chat.repository.ChatDetailRepository;
import hanium.dtc.domain.chat.repository.ChatRecordRepository;
import hanium.dtc.domain.chat.service.ChatService;
import hanium.dtc.exception.CommonException;
import hanium.dtc.exception.ErrorCode;
import hanium.dtc.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ChatDetailRepository chatDetailRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ObjectMapper objectMapper;

    private final JwtUtil jwtUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String query = session.getUri().getQuery();
        String token = getTokenFromQuery(query);

        if (token == null) {
            closeSession(session, "Invalid or missing token");
            return;
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.error("Invalid token: {}", token, e);
            closeSession(session, "Invalid token");
            return;
        }

        Long recordId = getRecordIdFromQuery(query);

        if (recordId == null) {
            closeSession(session, "Invalid query parameters");
            return;
        }

        ChatRecord chatRecord = chatRecordRepository.findById(recordId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_CHAT_RECORD));


        if (!isAuthorizedUser(userId, chatRecord)) {
            closeSession(session, "Unauthorized user for this chat");
            return;
        }

        chatService.addSessionToChatRecord(chatRecord, session);
        log.info("WebSocket connection established for user: " + userId + " in chat record: " + recordId);
    }

    private String getTokenFromQuery(String query) {
        if (query != null && query.contains("token=")) {
            return query.split("token=")[1].split("&")[0];
        }
        return null;
    }

    private Long getRecordIdFromQuery(String query) {
        if (query != null && query.contains("record_id=")) {
            try {
                return Long.parseLong(query.split("record_id=")[1].split("&")[0]);
            } catch (NumberFormatException e) {
                log.error("Invalid record_id format: " + query);
            }
        }
        return null;
    }

    private boolean isAuthorizedUser(Long userId, ChatRecord chatRecord) {
        return userId != null && (userId.equals(chatRecord.getUser().getId()) || userId.equals(chatRecord.getOtherId()));
    }

    private void closeSession(WebSocketSession session, String reason) {
        try {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason(reason));
            log.info("WebSocket connection closed: " + reason);
        } catch (IOException e) {
            log.error("Error closing session: ", e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        try {
            String payload = message.getPayload();
            ChatDetailRequest chatDetailRequest = objectMapper.readValue(payload, ChatDetailRequest.class);
            Long recordId = getRecordIdFromQuery(session.getUri().getQuery());

            ChatRecord chatRecord = chatRecordRepository.findById(recordId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER)); //수정 필요

            Long senderId = jwtUtil.getUserIdFromToken(getTokenFromQuery(session.getUri().getQuery()));

            ChatDetail chatDetail = ChatDetail.builder()
                    .chatRecord(chatRecord)
                    .senderId(senderId)
                    .content(chatDetailRequest.content())
                    .chatTime(LocalDateTime.now())
                    .type(chatDetailRequest.chatType())
                    .build();

            chatDetailRepository.save(chatDetail);

            chatRecord.setRecentChat(chatDetailRequest.content());
            chatRecord.setRecentTime(chatDetail.getChatTime());
            chatRecordRepository.save(chatRecord);

            ChatDetailResponse chatDetailResponse = new ChatDetailResponse(
                    chatDetail.getSenderId(),
                    chatDetail.getContent(),
                    chatDetail.getChatTime(),
                    chatDetail.getChatType()
            );

            Set<WebSocketSession> sessions = chatService.getSessionsByChatRecord(chatRecord);

            sendToEachSocket(sessions, new TextMessage(objectMapper.writeValueAsString(chatDetailResponse)));

        } catch (Exception e) {
            log.error("Error handling WebSocket message: ", e);
            try {
                session.sendMessage(new TextMessage("Error handling message: " + e.getMessage()));
            } catch (IOException ioException) {
                log.error("Error sending error message to client: ", ioException);
            }
        }
    }

    private void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message) {
        sessions.parallelStream().forEach(roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                log.error("Error sending message to session: ", e);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            log.info("WebSocket connection closed: " + session.getId() + ", status: " + status);
        } catch (Exception e) {
            log.error("Error during WebSocket connection closure: ", e);
        }
    }
}