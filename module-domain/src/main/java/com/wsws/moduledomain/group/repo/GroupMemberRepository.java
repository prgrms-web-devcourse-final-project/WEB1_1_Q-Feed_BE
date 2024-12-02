package com.wsws.moduledomain.group.repo;

import com.wsws.moduledomain.chat.ChatMessage;
import com.wsws.moduledomain.group.GroupMember;

public interface GroupMemberRepository {
    void save(GroupMember groupMember);
    void delete(GroupMember groupMember);
}
