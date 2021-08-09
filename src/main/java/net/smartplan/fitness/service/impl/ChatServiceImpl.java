package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.AdminChatRoomDTO;
import net.smartplan.fitness.dto.ChatMessagesDTO;
import net.smartplan.fitness.dto.ChatRoomDTO;
import net.smartplan.fitness.entity.ChatMessages;
import net.smartplan.fitness.entity.ChatRoom;
import net.smartplan.fitness.repository.ChatMessagesRepository;
import net.smartplan.fitness.repository.ChatRoomRepository;
import net.smartplan.fitness.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = "tenantTransactionManager")
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessagesRepository chatMessagesRepository;

    @Autowired
    public ChatServiceImpl(ChatRoomRepository chatRoomRepository, ChatMessagesRepository chatMessagesRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessagesRepository = chatMessagesRepository;
    }

    @Override
    public ChatMessagesDTO saveChatMessage(ChatMessagesDTO messagesDTO) {
        try{
            ChatRoom room ;
            ChatMessages chatMessages=  new ChatMessages();
            ChatRoom chatroom = chatRoomRepository.findBySenderAndReceiver(messagesDTO.getSender(), messagesDTO.getReceiver());

            if (chatroom ==null){
                chatroom = chatRoomRepository.findBySenderAndReceiver(messagesDTO.getReceiver(), messagesDTO.getSender());
            }

            if (chatroom!=null){

                if( !messagesDTO.getSender().equals("ADMIN")){
//                    net.epic.commons.config.security.dto.UserDTO buyerPic = authServiceClient.getUserDetails(TenantContextHolder.getTenant(), messagesDTO.getSender());
//                    if(buyerPic!=null){
//                        if(buyerPic.getImageUrl()!=null){
//                            chatroom.setProfilePic(buyerPic.getImageUrl());
//                        }
//                        if(buyerPic.getName()!=null){
//                            chatroom.setUsername(buyerPic.getName());
//                        }
//                    }
                }

                chatMessages.setChatRoomId(chatroom);
                chatroom.setUpdated(new Date());
                chatRoomRepository.save(chatroom);
            }else{
                room =  new ChatRoom();

//                net.epic.commons.config.security.dto.UserDTO buyerPic = authServiceClient.getUserDetails(TenantContextHolder.getTenant(), messagesDTO.getSender());
//                if( !messagesDTO.getSender().equals("ADMIN")){
//                    if(buyerPic!=null){
//                        if(buyerPic.getImageUrl()!=null){
//                            room.setProfilePic(buyerPic.getImageUrl());
//                        }
//                        if(buyerPic.getName()!=null){
//                            room.setUsername(buyerPic.getName());
//                        }
//                    }
//                }

                room.setReceiver(messagesDTO.getReceiver());
                room.setSender(messagesDTO.getSender());
                room.setUpdated(new Date());
                chatRoomRepository.save(room);

                chatMessages.setChatRoomId(room);
            }

            chatMessages.setContent(messagesDTO.getContent());
            chatMessages.setReceiver(messagesDTO.getReceiver());
            chatMessages.setSender(messagesDTO.getSender());
            chatMessages.setStatus("NEW");
            chatMessages.setTime(new Date());
            chatMessages.setUrl(messagesDTO.getUrl());

            chatMessagesRepository.save(chatMessages);
            return messagesDTO;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ChatRoomDTO> fetchChatRoomList(String sender) {
        List<ChatRoomDTO> dtoList = new ArrayList<>();
        ChatRoomDTO dto;
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllBySenderOrReceiver(sender,sender);

        if(chatRoomList!=null){
            ChatMessages chatMessages = new ChatMessages();
            for (ChatRoom room:chatRoomList) {
                dto= new ChatRoomDTO();
                dto.setChatRoomId(room.getChatRoomId());
                dto.setReceiver(room.getReceiver());
                dto.setSender(room.getSender());

                if(room.getChatMessagesCollection().size()>0){
                    dto.setMsgCount(room.getChatMessagesCollection().size());
                    for (ChatMessages element : room.getChatMessagesCollection()) {
                        chatMessages = element;
                    }

                    dto.setLastTime(chatMessages.getTime());
                }else{
                    dto.setMsgCount(0);
                }

                List<ChatMessages> notDelivered = chatMessagesRepository.findAllByChatRoomIdAndSenderEqualsAndStatusEquals(room, room.getReceiver(),"NEW");
                dto.setNewMessageCount(Math.max(notDelivered.size(), 0));

                dtoList.add(dto);
            }

            return dtoList;
        }else{
            return null;
        }
    }

    @Override
    public List<ChatMessagesDTO> fetchMessagesByChatRoomId(String chatRoomID) {
        List<ChatMessagesDTO> dtoList = new ArrayList<>();
        ChatMessagesDTO dto;
        Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(Integer.parseInt(chatRoomID));

        if(chatRoomList.isPresent()){
            if(!chatRoomList.get().getChatMessagesCollection().isEmpty()){
                for (ChatMessages messages :chatRoomList.get().getChatMessagesCollection()) {
                    dto= new ChatMessagesDTO();
                    dto.setChatMessagesId(messages.getChatMessagesId());
                    dto.setReceiver(messages.getReceiver());
                    dto.setSender(messages.getSender());
                    dto.setContent(messages.getContent());
                    dto.setStatus(messages.getStatus());
                    dto.setTime(messages.getTime());
                    dto.setUrl(messages.getUrl());
                    dtoList.add(dto);
                }
            }


            return dtoList;
        }else{
            return null;
        }
    }

    @Override
    public ChatMessagesDTO setAsDeliveredByChatRoomId(String chatRoomID  ,String sender) {
        ChatMessagesDTO dto = new ChatMessagesDTO();
        dto.setContent("Set AS Delivered");
        Optional<ChatRoom> chatRoomList = chatRoomRepository.findById(Integer.parseInt(chatRoomID));

        if(chatRoomList.isPresent()){
            ChatRoom room = chatRoomList.get();
            List<ChatMessages> notDelivered = chatMessagesRepository.findAllByChatRoomIdAndSenderEqualsAndStatusEquals(room, sender, "NEW");
            if(notDelivered.size()>0){
                for (ChatMessages messages : notDelivered) {
                    messages.setStatus("DELIVERED");
                    messages.setDeliveredTime(new Date());
                    chatMessagesRepository.save(messages);
                }
            }
        }
        return dto;
    }

    @Override
    public List<AdminChatRoomDTO> fetchAdminChatRoomList(String sender) {
        List<AdminChatRoomDTO> dtoList = new ArrayList<>();
        List<net.smartplan.fitness.dto.ChatMessagesDTO> chatMessagesDTOList;
        ChatMessagesDTO messagesDTO ;
        AdminChatRoomDTO dto;
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllBySenderOrReceiverOrderByUpdatedDesc(sender,sender);
        int newMessageCount=0;

        if(chatRoomList!=null){
            ChatMessages chatMessages = new ChatMessages();
            for (ChatRoom room:chatRoomList) {
                newMessageCount=0;

                dto= new AdminChatRoomDTO();
                dto.setChatRoomId(room.getChatRoomId());
                dto.setReceiver(room.getReceiver());
                dto.setSender(room.getSender());

                chatMessagesDTOList = new ArrayList<>();
                if(room.getChatMessagesCollection().size()>0){

                    for (ChatMessages messages : room.getChatMessagesCollection()) {
                        messagesDTO =  new ChatMessagesDTO();
                        messagesDTO.setChatMessagesId(messages.getChatMessagesId());
                        messagesDTO.setReceiver(messages.getReceiver());
                        messagesDTO.setSender(messages.getSender());
                        messagesDTO.setContent(messages.getContent());
                        messagesDTO.setStatus(messages.getStatus());
                        messagesDTO.setTime(messages.getTime());
                        messagesDTO.setUrl(messages.getUrl());
                        chatMessagesDTOList.add(messagesDTO);
                        chatMessages = messages;

                        if(!messagesDTO.getSender().equals("ADMIN") && messagesDTO.getStatus().equals("NEW")){
                            newMessageCount ++;
                        }

                    }

                    dto.setLastTime(chatMessages.getTime());
                }

                dto.setChatMessagesCollection(chatMessagesDTOList);
                dto.setNewMessageCount(newMessageCount);
                dto.setProfilePic(room.getProfilePic());
                dto.setUsername(room.getUsername());

                dtoList.add(dto);
            }

            return dtoList;
        }else{
            return null;
        }
    }

    @Override
    public String getMessageCount() {
        long count = chatRoomRepository.count();
        return count+"";
    }


}
