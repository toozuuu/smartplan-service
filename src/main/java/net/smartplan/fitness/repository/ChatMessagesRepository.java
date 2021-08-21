package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.ChatMessages;
import net.smartplan.fitness.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatMessages,Integer> {
    List<ChatMessages> findAllByChatRoomIdAndSenderEqualsAndStatusEquals(ChatRoom chatRoom,String sender, String status);
}
