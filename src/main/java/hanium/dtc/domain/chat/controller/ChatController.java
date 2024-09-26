package hanium.dtc.domain.chat.controller;

import hanium.dtc.annotation.UserId;
import hanium.dtc.domain.chat.dto.request.ChatRecordRequest;
import hanium.dtc.domain.chat.dto.response.ChatDetailListResponse;
import hanium.dtc.domain.chat.dto.response.ChatRecordListResponse;
import hanium.dtc.domain.chat.service.ChatService;
import hanium.dtc.global.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseDto<Boolean> createChatRoom(@RequestBody ChatRecordRequest chatRecordRequest) {
        return ResponseDto.ok(chatService.createChatRecord(chatRecordRequest));
    }

    @GetMapping("/rooms/")
    public ResponseDto<ChatRecordListResponse> getChatRooms(@UserId Long userId) {
        return ResponseDto.ok(chatService.chatRecordList(userId));
    }

    @GetMapping("/detail/{recordId}")
    public ResponseDto<ChatDetailListResponse> getChatDetails(@UserId Long userId,  @PathVariable Long recordId) {
        return ResponseDto.ok(chatService.chatDetailList(userId ,recordId));
    }
}