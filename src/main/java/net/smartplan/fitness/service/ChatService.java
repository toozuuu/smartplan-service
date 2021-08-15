package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.AdminChatRoomDTO;
import net.smartplan.fitness.dto.ChatMessagesDTO;
import net.smartplan.fitness.dto.ChatRoomDTO;

import java.util.List;

public interface ChatService {
    ChatMessagesDTO saveChatMessage(ChatMessagesDTO messagesDTO);

    List<ChatRoomDTO> fetchChatRoomList(String sender);

    List<ChatMessagesDTO> fetchMessagesByChatRoomId(String chatRoomID);

    ChatMessagesDTO setAsDeliveredByChatRoomId(String chatRoomID, String sender);

    List<AdminChatRoomDTO> fetchAdminChatRoomList(String sender);

    String getMessageCount();

}
