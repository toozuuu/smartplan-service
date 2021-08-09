package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AdminChatRoomDTO {
    private Integer chatRoomId;
    private String sender;
    private String receiver;
    private Date lastTime;
    private Integer newMessageCount;
    private List<net.smartplan.fitness.dto.ChatMessagesDTO> chatMessagesCollection;
    private String profilePic;
    private String username;
}
