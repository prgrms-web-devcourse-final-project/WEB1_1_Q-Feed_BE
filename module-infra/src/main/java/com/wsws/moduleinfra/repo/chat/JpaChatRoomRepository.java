package com.wsws.moduleinfra.repo.chat;

import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduledomain.chat.repo.ChatRoomRepository;
import com.wsws.moduleinfra.entity.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, ChatRoomRepository {

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE cr.userId = :userId AND cr.userId2 = :userId2")
    Optional<ChatRoom> findChatRoomBetweenUsers(@Param("userId") String userId, @Param("userId2") String userId2);

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE cr.userId = :userId OR cr.userId2 = :userId")
    List<ChatRoom> findChatRooms(@Param("userId") String userId);

    Optional<ChatRoom> findChatRoomById(Long chatRoomId);



//    @Query("SELECT new com.wsws.moduleinfra.repo.chat.dto.ChatRoomInfraDTO(" +
//            "cr.id, " +  // ChatRoom ID
//            "CASE WHEN cr.userId = :user3 THEN u2.nickname ELSE u1.nickname END, " +  // 상대방의 닉네임
//            "CASE WHEN cr.userId = :user3 THEN u2.profileImage ELSE u1.profileImage END, " +  // 상대방의 프로필 이미지
//            "cm.content, " +  // 마지막 메시지 내용
//            "cm.createdAt " +  // 마지막 메시지 시간
//            ") " +
//            "FROM ChatRoom cr " +
//            "LEFT JOIN User u1 ON cr.userId = u1.id " +  // 첫 번째 사용자
//            "LEFT JOIN User u2 ON cr.userId2 = u2.id " + // 두 번째 사용자
//            "LEFT JOIN ChatMessage cm ON cr.id = cm.chatRoom.id " +  // 채팅 메시지
//            "WHERE cr.userId.value = :user3 OR cr.userId2.value = :user3 " +  // user_id 컬럼으로 비교
//            "ORDER BY cm.createdAt DESC")
//    List<ChatRoomInfraDTO> findChatRoomsWithUserDetails(@Param("user3") String user3);
}
