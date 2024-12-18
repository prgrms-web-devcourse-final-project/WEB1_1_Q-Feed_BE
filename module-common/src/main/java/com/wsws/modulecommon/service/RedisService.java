package com.wsws.modulecommon.service;

public interface RedisService {
    /**
     * 특정 사용자와 채팅방을 매핑하여 저장합니다.
     *
     * @param userId
     * @param roomId
     */
    void saveUserRoomMapping(String userId, String roomId);

    /**
     * 특정 사용자가 연결된 채팅방을 조회합니다.
     *
     * @param userId
     * @return
     */
    String getUserCurrentRoom(String userId);

    /**
     * 특정 사용자와 채팅방 간의 매핑을 제거합니다.
     *
     * @param userId
     */
    void removeUserRoomMapping(String userId);

    void saveSessionUserMapping(String sessionId, String userId);

    String getUserBySessionId(String sessionId);

    void removeSessionUserMapping(String sessionId);
}
