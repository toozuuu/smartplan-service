package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.AdminChatRoomDTO;
import net.smartplan.fitness.dto.ChatMessagesDTO;
import net.smartplan.fitness.dto.ChatRoomDTO;
import net.smartplan.fitness.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/save")
    public ChatMessagesDTO saveMessage(@RequestBody ChatMessagesDTO messagesDTO) {
        return chatService.saveChatMessage(messagesDTO);
    }

    @GetMapping("/fetchChatRoomList/{sender}")
    public List<ChatRoomDTO> fetchChatRoomList(@PathVariable String sender) {

        return chatService.fetchChatRoomList(sender);
    }

    @GetMapping("/fetchMessagesByChatRoomId/{chatroomid}")
    public List<ChatMessagesDTO> fetchMessagesByChatRoomId(@PathVariable String chatroomid) {

        return chatService.fetchMessagesByChatRoomId(chatroomid);
    }

    @GetMapping("/setAsDeliveredByChatRoomId/{chatroomid}/{sender}")
    public ChatMessagesDTO setAsDeliveredByChatRoomId(@PathVariable String chatroomid,@PathVariable String sender) {

        return chatService.setAsDeliveredByChatRoomId(chatroomid, sender);
    }

    @GetMapping("/fetchAdminChatRoomList/{sender}")
    public List<AdminChatRoomDTO> fetchAdminChatRoomList(@PathVariable String sender) {

        return chatService.fetchAdminChatRoomList(sender);
    }

    @GetMapping("/getMessageCount")
    public String getMessageCount() {
        return chatService.getMessageCount();
    }
}
