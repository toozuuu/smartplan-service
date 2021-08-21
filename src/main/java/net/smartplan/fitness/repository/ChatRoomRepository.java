package net.smartplan.fitness.repository;

import net.smartplan.fitness.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Integer> {
    ChatRoom findBySenderAndReceiver(String sender, String receiver);
    List<ChatRoom> findAllBySenderOrReceiver(String sender, String receiver);
    List<ChatRoom> findAllBySenderOrReceiverOrderByUpdatedDesc(String sender, String receiver);
}
