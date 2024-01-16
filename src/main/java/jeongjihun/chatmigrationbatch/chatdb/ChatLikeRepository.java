package jeongjihun.chatmigrationbatch.chatdb;

import jeongjihun.chatmigrationbatch.chatdb.entity.Chat;
import jeongjihun.chatmigrationbatch.chatdb.entity.ChatLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

public interface ChatLikeRepository extends JpaRepository<ChatLike, Long> {

    @Query("SELECT c FROM ChatLike c WHERE c.createdAt < :targetTime")
    Page<Chat> findChatLikeForTest(LocalDateTime maxDateTime, Pageable pageable);
}
