package net.smartplan.fitness.entity;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "chat_messages")
@XmlRootElement
@Data
public class ChatMessages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "chat_messages_id")
    private Integer chatMessagesId;
    @Lob
    @Column(name = "content")
    private String content;
    @Lob
    @Column(name = "url")
    private String url;
    @Column(name = "sender")
    private String sender;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "status")
    private String status;
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "delivered_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredTime;
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id")
    @ManyToOne(optional = false)
    private ChatRoom chatRoomId;

}
