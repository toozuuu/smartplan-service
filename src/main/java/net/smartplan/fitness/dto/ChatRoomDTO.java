package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatRoomDTO {
    private Integer chatRoomId;
    private String sender;
    private String receiver;
    private Date lastTime;
    private Integer msgCount;
    private Integer newMessageCount;
}
