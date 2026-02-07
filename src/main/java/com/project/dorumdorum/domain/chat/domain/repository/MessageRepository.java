package com.project.dorumdorum.domain.chat.domain.repository;

import com.project.dorumdorum.domain.chat.domain.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {
    
    List<Message> findByMessageRoomNoOrderByMessageNoDesc(Long messageRoomNo, Pageable pageable);
    
    List<Message> findByMessageRoomNoAndMessageNoLessThanOrderByMessageNoDesc(
            Long messageRoomNo, 
            Long cursor, 
            Pageable pageable
    );
}
