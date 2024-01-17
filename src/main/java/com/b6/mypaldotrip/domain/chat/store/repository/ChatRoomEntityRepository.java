package com.b6.mypaldotrip.domain.chat.store.repository;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomEntityRepository extends MongoRepository<ChatRoomEntity, String> {

    Optional<ChatRoomEntity> findByChatRoomId(String chatRoomId);

    Optional<ChatRoomEntity> findByChatRoomName(String chatRoomName);
}