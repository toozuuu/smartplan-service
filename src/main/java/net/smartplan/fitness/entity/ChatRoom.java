package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "chat_room")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @ToString.Exclude
    private Collection<ChatMessages> chatMessagesCollection;


    @XmlTransient
    public Collection<net.smartplan.fitness.entity.ChatMessages> getChatMessagesCollection() {
        return chatMessagesCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(chatRoomId, chatRoom.chatRoomId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
