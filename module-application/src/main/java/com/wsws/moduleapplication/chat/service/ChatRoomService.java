package com.wsws.moduleapplication.chat.service;

import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduleapplication.chat.exception.AlreadyChatRoomException;
import com.wsws.moduleapplication.chat.exception.ChatRoomNotFoundException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatMessageRepository;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Nickname;
import com.wsws.moduledomain.user.vo.UserId;

import com.wsws.moduleinfra.repo.chat.ChatRoomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wsws.moduleapplication.chat.exception.ChatServiceErrorCode.CHATROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository  chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createChatRoom(ChatRoomServiceRequest req) {
        // 기존 채팅방이 존재하는지 확인
        if (chatRoomRepository.findChatRoomBetweenUsers(req.userId(), req.userId2()).isPresent()) {
            throw AlreadyChatRoomException.EXCEPTION;
        }
        //사용자 존재 확인
        getUserById(req.userId());
        getUserById(req.userId2());
        ChatRoom chatRoom = ChatRoom.create(null,req.userId(), req.userId2(), LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId,String userId) {
        ChatRoom chatRoom = getChatRoomById(chatRoomId);
        // 채팅방 소유자 확인
        checkOwnership(chatRoom, userId);

        chatRoomRepository.deleteById(chatRoomId);
    }

    //채팅방 목록 조회
    public List<ChatRoomServiceResponse> getChatRooms(String userId) {

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRooms(userId);

        // 상대방 사용자 정보
        Map<Long, User> otherUserMap = getOtherUsersMap(chatRooms, userId);

        // 각 채팅방에 대한 마지막 메시지
        Map<Long, ChatMessage> lastMessageMap = getLastMessagesForChatRooms(chatRooms);

        // 읽지 않은 메시지 개수
        Map<Long, Long> unreadCountMap = getUnreadCountsForChatRooms(chatRooms, userId);

        return chatRooms.stream().map(chatRoom -> new ChatRoomServiceResponse(
                chatRoom, otherUserMap.get(chatRoom.getId()), lastMessageMap.get(chatRoom.getId()), unreadCountMap.get(chatRoom.getId())
        )).collect(Collectors.toList());
    }

    //채팅방 찾기
    public ChatRoomServiceResponse getChatRoomWithOtherUser(String userId, String nickname) {
        // nickname으로 reqUserId 찾기
        User otherUser = getUserByNickname(nickname);

        ChatRoom chatRoom = findChatRoomBetweenUsers(userId, otherUser.getId().getValue());

        // 마지막 메시지 가져오기
        ChatMessage lastMessage = getLastMessage(chatRoom.getId());

        // 읽지 않은 메시지 개수 가져오기
        long unreadCount = chatMessageRepository.countUnreadMessages(chatRoom.getId(), otherUser.getId().getValue());

        return new ChatRoomServiceResponse(
                chatRoom, otherUser, lastMessage, unreadCount
        );
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findChatRoomById(chatRoomId)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private ChatRoom findChatRoomBetweenUsers(String userId, String userId2) {
        return chatRoomRepository.findChatRoomBetweenUsers(userId, userId2)
                .orElseThrow(() -> ChatRoomNotFoundException.EXCEPTION);
    }

    private User getUserById(String senderId) {
        return userRepository.findById(UserId.of(senderId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(Nickname.from(nickname))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    // 다른 사용자의 정보 가져오기
    private User getOtherUser(ChatRoom chatRoom, String userId) {
        String otherUserId = chatRoom.getUserId().equals(userId) ? chatRoom.getUserId2() : chatRoom.getUserId();
        return getUserById(otherUserId);
    }

    // 마지막 메시지 가져오기
    private ChatMessage getLastMessage(Long chatRoomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
                .orElse(null);  // 메시지가 없으면 null 반환
    }

    // 채팅방의 소유자가 현재 사용자인지 확인
    private void checkOwnership(ChatRoom chatRoom, String userId) {
        if (!chatRoom.getUserId().equals(userId) && !chatRoom.getUserId2().equals(userId)) {
            // 소유자가 아니면 예외를 발생시켜 권한이 없음을 알림
            throw new IllegalStateException("채팅방을 삭제할 권한이 없습니다.");

        }
    }

    // 상대방 정보 가져오기
    private Map<Long, User> getOtherUsersMap(List<ChatRoom> chatRooms, String userId) {
        Map<Long, User> otherUserMap = new HashMap<>();
        for (ChatRoom chatRoom : chatRooms) {
            otherUserMap.put(chatRoom.getId(), getOtherUser(chatRoom, userId));
        }
        return otherUserMap;
    }

    // 마지막 메시지 가져오기
    private Map<Long, ChatMessage> getLastMessagesForChatRooms(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .collect(Collectors.toMap(
                        ChatRoom::getId,
                        chatRoom -> getLastMessage(chatRoom.getId())
                ));
    }

    // 읽지 않은 메시지 개수 가져오기
    private Map<Long, Long> getUnreadCountsForChatRooms(List<ChatRoom> chatRooms, String userId) {
        return chatRooms.stream()
                .collect(Collectors.toMap(
                        ChatRoom::getId,
                        chatRoom -> chatMessageRepository.countUnreadMessages(chatRoom.getId(), userId)
                ));
    }
}