package net.smartplan.fitness.entity;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "chat_room")
@XmlRootElement
@Data
public class ChatRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "chat_room_id")
    private Integer chatRoomId;
    @Basic(optional = false)
    @Column(name = "sender")
    private String sender;
    @Basic(optional = false)
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Column(name = "profile_pic")
    private String profilePic;
    @Column(name = "username")
    private String username;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoomId")
    private Collection<ChatMessages> chatMessagesCollection;


    @XmlTransient
    public Collection<net.smartplan.fitness.entity.ChatMessages> getChatMessagesCollection() {
        return chatMessagesCollection;
    }

    public void setChatMessagesCollection(Collection<net.smartplan.fitness.entity.ChatMessages> chatMessagesCollection) {
        this.chatMessagesCollection = chatMessagesCollection;
    }

}
