package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessagesDTO {
    private Integer chatMessagesId;
    private String content;
    private String url;
    private String sender;
    private String receiver;
    private String status;
    private Date time;
    private Date deliveredTime;
}
